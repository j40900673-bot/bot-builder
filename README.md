# Schematic Builder Bot

Fabric-Mod-Projekt für **Minecraft Java 1.21.11**. Das Menü steuert Baritones legitimen Schematic-Bauprozess und übergibt die aktuell in Litematica geladene Placement an Baritone. Bewegung, Werkzeuggebrauch, Abbau und Platzierung werden von Baritone über die normalen Spielinteraktionen erledigt.

## Voraussetzungen im Spiel

- Minecraft Java 1.21.11
- Fabric Loader und Fabric API
- Litematica plus MaLiLib, jeweils passend für 1.21.11
- **Baritone für Minecraft 1.21.11**

Die Baritone-Integration ist optional zur Laufzeit und wird nicht in dieser Mod-JAR gebündelt. Baritones [offizieller Quellcode-Branch für 1.21.11](https://github.com/cabaletta/baritone/tree/1.21.11) enthält den erforderlichen Litematica-Befehl. Verwende einen Build genau dieses Branches; ältere Baritone-JARs sind nicht kompatibel. [Litematica-Downloads](https://modrinth.com/mod/litematica/versions?g=1.21.11) · [MaLiLib-Downloads](https://modrinth.com/mod/malilib/versions?g=1.21.11)

## Funktionen

- **O** öffnet das ClickGUI
- Start übergibt `litematica <Placement-Nr.>` an Baritone
- Placement-Nummer, Pause, Fortsetzen und Abbrechen
- Statusabfrage des Baritone-Bauprozesses
- In Multiplayerwelten startet der Bot erst nach manuellem Bestätigen, dass die Serverregeln geprüft wurden
- GUI-Einstellungen werden in `config/schematic-builder-bot.json` gespeichert

Baritone hält an, wenn notwendige Materialien fehlen oder seine normalen Spielinteraktionen den nächsten Schritt nicht ausführen können. Das Menü führt keine Berechtigungs- oder Anti-Cheat-Umgehung aus. Prüfe vor dem Start die Regeln des Servers. Materialentnahme aus Truhen/Shulkerboxen und ein eigener Wiederaufnahme- oder Fortschrittsspeicher sind in dieser Mod noch nicht implementiert.

## Bauen

Benötigt Java 21 oder neuer.

Windows:

```powershell
.\gradlew.bat build
```

Linux/macOS:

```bash
bash gradlew build
```

Die Mod-JAR liegt anschließend in `build/libs/`.

## Neues GitHub-Repository

1. ZIP herunterladen und entpacken.
2. Auf GitHub ein neues leeres Repository erstellen.
3. Den gesamten Ordnerinhalt einschließlich `.github`, `gradle`, `gradlew` und `gradlew.bat` hochladen. Alternativ im Projektordner:

```bash
git init
git add .
git commit -m "Add Schematic Builder Bot"
git branch -M main
git remote add origin https://github.com/DEIN-NAME/DEIN-REPO.git
git push -u origin main
```

GitHub Actions baut das Projekt bei jedem Push und Pull Request. Die JAR wird im Actions-Lauf als Artifact bereitgestellt.

## Lizenz

Der Projektcode steht unter MIT; siehe [LICENSE](LICENSE). Baritone ist eine separate LGPL-3.0-Mod und wird nicht mitgeliefert.
