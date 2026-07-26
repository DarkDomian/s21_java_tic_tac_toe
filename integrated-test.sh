#!/bin/bash

BASE_URL="${1:-http://localhost:8080}"
PASS=0
FAIL=0

RED='\033[0;31m'; GREEN='\033[0;32m'; NC='\033[0m'

uuid() { python3 -c "import uuid; print(uuid.uuid4())"; }

call() {
    curl -s -w "\n%{http_code}" -X POST "$BASE_URL/game/$1" \
        -H "Content-Type: application/json" \
        -d "{\"board\":$2}"
}

code_of() { echo "$1" | tail -1; }
body_of() { echo "$1" | head -n -1; }

add_user_move() {
    python3 -c "
import json, sys
b = json.loads('$1')
for r in range(3):
    for c in range(3):
        if b[r][c] == 0:
            b[r][c] = 1
            print(json.dumps(b))
            sys.exit()
"
}

count_filled() {
    python3 -c "import json; b=json.loads('$1'); print(sum(1 for r in range(3) for c in range(3) if b[r][c]!=0))"
}

has_board() {
    python3 -c "import json,sys; d=json.load(sys.stdin); print('1' if 'board' in d else '0')"
}

check() {
    local desc="$1" exp="$2" act="$3"
    if [ "$exp" = "$act" ]; then
        echo -e "  ${GREEN}✓${NC} $desc"; PASS=$((PASS+1))
    else
        echo -e "  ${RED}✗${NC} $desc (expected $exp, got $act)"; FAIL=$((FAIL+1))
    fi
}

# Play until game ends, output the last valid board state
play_until_end() {
    local id="$1" board='[[0,0,0],[0,0,0],[0,0,0]]' saved=''
    for i in $(seq 1 9); do
        board=$(add_user_move "$board")
        resp=$(call "$id" "$board")
        code=$(code_of "$resp")
        [ "$code" != "200" ] && break
        body=$(body_of "$resp")
        if echo "$body" | has_board | grep -q 1; then
            saved=$(echo "$body" | python3 -c "import json,sys; print(json.dumps(json.load(sys.stdin)['board']))")
            board="$saved"
        else
            break
        fi
    done
    echo "$saved"
}

echo "=========================================="
echo "  Tic-Tac-Toe Integration Tests"
echo "=========================================="
echo

# ─────────────────────────────────────────────────
echo "=== 1. Empty board → 400 ==="
ID=$(uuid)
resp=$(call "$ID" '[[0,0,0],[0,0,0],[0,0,0]]')
check "HTTP 400" "400" "$(code_of "$resp")"
echo

# ─────────────────────────────────────────────────
echo "=== 2. Wrong symbol (-1) → 400 ==="
ID=$(uuid)
resp=$(call "$ID" '[[0,0,0],[0,-1,0],[0,0,0]]')
check "HTTP 400" "400" "$(code_of "$resp")"
echo

# ─────────────────────────────────────────────────
echo "=== 3. First valid move → 200 ==="
ID=$(uuid)
resp=$(call "$ID" '[[1,0,0],[0,0,0],[0,0,0]]')
check "HTTP 200" "200" "$(code_of "$resp")"
board=$(body_of "$resp" | python3 -c "import json,sys; print(json.dumps(json.load(sys.stdin)['board']))")
check "Two cells filled (1 + -1)" "2" "$(count_filled "$board")"
echo

# ─────────────────────────────────────────────────
echo "=== 4. Overwrite computer's cell → 400 ==="
resp=$(call "$ID" '[[1,9,0],[0,0,0],[0,0,0]]')
check "HTTP 400" "400" "$(code_of "$resp")"
echo

# ─────────────────────────────────────────────────
echo "=== 5. Two user moves at once → 400 ==="
ID=$(uuid)
call "$ID" '[[1,0,0],[0,0,0],[0,0,0]]' > /dev/null
resp=$(call "$ID" '[[1,1,0],[1,0,0],[0,0,0]]')
check "HTTP 400" "400" "$(code_of "$resp")"
echo

# ─────────────────────────────────────────────────
echo "=== 6. Wrong dimensions → 400 ==="
ID=$(uuid)
resp=$(call "$ID" '[[0,0],[0,0]]')
check "HTTP 400" "400" "$(code_of "$resp")"
echo

# ─────────────────────────────────────────────────
echo "=== 7. Two games in parallel ==="
ID_A=$(uuid)
ID_B=$(uuid)
echo "  Playing Game A..."
final_a=$(play_until_end "$ID_A")
echo "  Playing Game B..."
final_b=$(play_until_end "$ID_B")
check "Game A progressed" "1" "$(python3 -c "print('1' if len('$final_a') > 10 else '0')")"
check "Game B progressed" "1" "$(python3 -c "print('1' if len('$final_b') > 10 else '0')")"
echo

# ─────────────────────────────────────────────────
echo "=== 8. Full game — user=1, computer=-1 ==="
ID=$(uuid)
board='[[0,1,0],[0,0,0],[0,0,0]]'
resp=$(call "$ID" "$board")
check "First move 200" "200" "$(code_of "$resp")"
board=$(body_of "$resp" | python3 -c "import json,sys; print(json.dumps(json.load(sys.stdin)['board']))")
echo "  Round 1: filled=$(count_filled "$board")"

board=$(add_user_move "$board")
resp=$(call "$ID" "$board")
check "Second move 200" "200" "$(code_of "$resp")"
board=$(body_of "$resp" | python3 -c "import json,sys; print(json.dumps(json.load(sys.stdin)['board']))")
echo "  Round 2: filled=$(count_filled "$board")"

board=$(add_user_move "$board")
resp=$(call "$ID" "$board")
check "Third move 200" "200" "$(code_of "$resp")"
board=$(body_of "$resp" | python3 -c "import json,sys; print(json.dumps(json.load(sys.stdin)['board']))")
echo "  Round 3: filled=$(count_filled "$board")"
echo

# ─────────────────────────────────────────────────
echo "=== 9. Resend ended-game board → message ==="
ID=$(uuid)
final_board=$(play_until_end "$ID")
if [ -n "$final_board" ]; then
    resp=$(call "$ID" "$final_board")
    code=$(code_of "$resp")
    check "HTTP 200" "200" "$code"
    has_msg=$(body_of "$resp" | python3 -c "import json,sys; d=json.load(sys.stdin); print('1' if 'message' in d else '0')")
    check "Has message" "1" "$has_msg"
else
    check "Final board available" "non-empty" "(empty)"
fi
echo

# ─────────────────────────────────────────────────
echo "=========================================="
echo -e "  ${GREEN}$PASS passed${NC}, ${RED}$FAIL failed${NC}"
echo "=========================================="
[ "$FAIL" -eq 0 ]
