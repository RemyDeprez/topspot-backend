# Gemini Project Guidelines

## Injection de Dépendances

Pour maintenir un code propre, testable et maintenable, toutes les dépendances dans les beans Spring (services, contrôleurs, etc.) doivent être injectées via le constructeur.

### Règle

- **Utiliser l'injection par constructeur** pour toutes les dépendances.
- Rendre les champs de dépendance `final` pour garantir leur immuabilité après l'initialisation.
- Utiliser l'annotation `@RequiredArgsConstructor` de Lombok pour générer automatiquement le constructeur lorsque toutes les dépendances sont `final`.

### Exemple

**À ne pas faire (injection par champ) :**
```java
@Service
public class MyService {
    @Autowired
    private MyRepository myRepository;
}
```

**À faire (injection par constructeur avec Lombok) :**
```java
@Service
@RequiredArgsConstructor
public class MyService {
    private final MyRepository myRepository;
}
```

**À faire (injection par constructeur manuelle) :**
```java
@Service
public class MyService {
    private final MyRepository myRepository;

    @Autowired
    public MyService(MyRepository myRepository) {
        this.myRepository = myRepository;
    }
}
```

