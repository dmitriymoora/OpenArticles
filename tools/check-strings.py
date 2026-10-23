import re
import sys
from pathlib import Path

RES = Path(__file__).resolve().parent.parent / "shared/src/commonMain/composeResources"

def load(folder):
    text = (RES / folder / "strings.xml").read_text(encoding="utf-8")
    result = {}
    for name, value in re.findall(r'<string name="([^"]+)">(.*?)</string>', text, re.S):
        result[name] = sorted(re.findall(r"%\d+\$[sd]", value))
    return result

base = load("values")
ru = load("values-ru")
problems = []
for key in base:
    if key not in ru:
        problems.append(f"нет в values-ru: {key}")
    elif base[key] != ru[key]:
        problems.append(f"разные аргументы: {key}")
for key in ru:
    if key not in base:
        problems.append(f"нет в values: {key}")

if problems:
    print("\n".join(problems))
    sys.exit(1)
print(f"0 расхождений, ключей {len(base)}")
