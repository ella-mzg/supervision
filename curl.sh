#!/usr/bin/env bash
set -euo pipefail

BASE_URL="${BASE_URL:-http://localhost:8080}"
API="$BASE_URL/api/todos"

echo "== Create todo =="
CREATE1=$(curl -sS -X POST "$API" \
  -H "Content-Type: application/json" \
  -d '{"title":"Acheter du pain"}')
echo "$CREATE1"

ID1=$(echo "$CREATE1" | sed -n 's/.*"id":[ ]*\([0-9]\+\).*/\1/p')
if [[ -z "${ID1:-}" ]]; then
  echo "Could not parse id from response"
  exit 1
fi

echo
echo "== List todos =="
curl -sS "$API" | cat
echo

echo
echo "== Get todo by id ($ID1) =="
curl -sS "$API/$ID1" | cat
echo

echo
echo "== Update todo (mark done) =="
curl -sS -X PUT "$API/$ID1" \
  -H "Content-Type: application/json" \
  -d '{"title":"Acheter du pain","done":true}' | cat
echo

echo
echo "== Create another todo =="
curl -sS -X POST "$API" \
  -H "Content-Type: application/json" \
  -d '{"title":"Sortir les poubelles"}' | cat
echo

echo
echo "== List todos (after updates) =="
curl -sS "$API" | cat
echo

echo
echo "== Delete first todo ($ID1) =="
curl -sS -X DELETE "$API/$ID1" -i | sed -n '1,5p'
echo

echo
echo "== List todos (after delete) =="
curl -sS "$API" | cat
echo

echo
echo "Done."
