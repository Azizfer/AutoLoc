### D. Analyse SonarQube for IDE

L'analyse du package `web.controller` du projet AutoLoc a détecté **43 problèmes répartis dans 7 fichiers Java**.

Les problèmes sont regroupés en quatre catégories : code Java commenté (12), annotations `@ApiResponses` redondantes (20), exposition directe des entités JPA (10) et utilisation d'une date sans fuseau horaire explicite (1).

**Anomalie 1 — Annotations Swagger redondantes**

- **Problème :** utilisation du conteneur `@ApiResponses` alors que `@ApiResponse` peut être répétée directement.
- **Correction :** suppression du conteneur `@ApiResponses` et utilisation directe de plusieurs annotations `@ApiResponse`.
- **Résultat à vérifier :** disparition des signalements correspondants après une nouvelle analyse.

**Anomalie 2 — Fuseau horaire implicite**

- **Fichier :** `GlobalExceptionHandler.java`
- **Problème :** utilisation de `LocalDateTime.now()` sans préciser de fuseau horaire.
- **Correction :** remplacement par `OffsetDateTime.now(ZoneOffset.UTC)`.
- **Résultat à vérifier :** disparition du signalement relatif au fuseau horaire.

**Autres observations**

Les blocs de code Java commentés doivent être supprimés lorsqu'ils ne servent plus.

Les contrôleurs utilisent actuellement des entités JPA directement dans les requêtes et réponses HTTP. SonarQube recommande l'utilisation de DTOs afin de mieux séparer le modèle de persistance du contrat de l'API. Cette amélioration architecturale reste à réaliser dans une évolution future du projet.

**Bilan :** après les corrections simples, le nombre de problèmes devrait diminuer de 43 à environ 10, sous réserve d'une nouvelle analyse.