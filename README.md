# MagellanGPT

![alt text](<doc/banner.png>)

## Faire fonctionner l'authentification avec Azure AD

1) Générer une clé de keystore :

```shell
keytool -exportcert -alias androiddebugkey -keystore ~/.android/debug.keystore | openssl sha1 -binary | openssl base64
```

Windows : 
```shell
keytool -exportcert -alias androiddebugkey -keystore %HOMEPATH%\.android\debug.keystore | openssl sha1 -binary | openssl base64
```
Le mot de passe par défaut est : **android**

Récuperez la clé générée

3) Ensuite il faut se rendre sur https://portal.azure.com/#view/Microsoft_AAD_RegisteredApps/ApplicationMenuBlade/~/Authentication/appId/4cf58850-84de-4db1-b613-ab25d7dac819/isMSAApp~/false



4) Il faut ajouter une nouvelle ligne :
Nom de package : fr.group5.magellangpt
Hachage de signature : collez la valeur générée
Récupérez le redirectUri et le changer dans app/src/debug/res/raw/auth_config.json


## Architecture générale

### Couche présentation

#### MainActivity

La couche présentation contient toutes les features de l'application (login, main etc...). À la racine du package **presentation** on peut voir [MainActivity](./app/src/main/java/fr/group5/magellangpt/presentation/MainActivity.kt). Nous utilisons une application en Single Activity. Nous auront donc une seule et unique Activité. Dans cette activité nous déclarons notre Scaffold et notre seul NavHost. C'est donc au sein de ce Scaffold que la navigation sera effectuée.

Dans cette Activity nous injectons notre `navigator : Navigator`, cette instance de Navigator va nous permettre d'écouter toutes les navigations effectuée dans l'application et les répercuter sur notre navController. Cette classe nous permet d'effectuer la navigation directement depuis nos ViewModels.

Nous injectons aussi notre `errorHelper: ErrorHelper`. Tout comme pour le navigator, nous nous abonnons à cette instance pour pouvoir afficher les messages d'erreur en utilisant le snackbarHostState de notre Scaffold. Nous avons créé ce ErrorHelper pour éviter de passer des callback dans chacun de nos screens pour afficher des messages d'erreurs.


### Screen

Les screen sont des fonctions composables qui définissent l'affichage de nos écrans. Chaque Screen prend un State en paramètre et un callback onEvent(). Nous pouvons donc personnaliser nos Previews en modifiant le state. Les previews sont des fonctions composables privées situées en bas de page.

```kotlin
@Composable
fun LoginScreen(uiState : LoginUiState, onEvent : (LoginEvent) -> Unit){

}
```

### UiState

Les UiState sont des dataclass contenant toutes les données de nos écrans qui peuvent changer. Toutes les propriétés de ces states doivent être immutables et doivent donc être changées en updatant le state tout entier. Les states sont modifiés dans le ViewModel de cette manière :

```kotlin
private val _uiState = MutableStateFlow(LoginUiState())
val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

private fun onLogin(username : String, password : String){
    _uiState.update { it.copy(loading = false) }
}
```

### Event

Les event sont des SealedClass qui contiennent toutes les actions que le Screen peut déclencher. Ces actions sont traités dans le ViewModel :

```kotlin
sealed class LoginEvent {
    data object OnAppearing : LoginEvent()
    data class OnLogin(val username : String, val password : String) : LoginEvent()
}

fun onEvent(event : LoginEvent){
    when(event){
        is LoginEvent.OnAppearing -> onAppearing()
        is LoginEvent.OnLogin -> onLogin(event.username, event.password)
    }
}
```

### ViewModel

Le ViewModel est la pièce centrale de la couche présentation. Ces classes **handle** traitent tous les évenements propagés par l'interface utilisateur (Screen) et mettent à jour le state (UiState). Pour certaines méthodes, nous pouvons lancer des coroutines en utilisant le viewModelScope. Cela permet d'executer de longues tâches sans bloquer l'interface utilisateur.


### Package components

Le package components ne contient que des fonctions composables. Ces dernières doivent être le plus simple et réutilisables possibles. Ces composants peuvent être triés par feature si ils ne sont utilisés que dans une seule et même feature. Ces composants doivent avoir des preview, cela permettra de les reconnaitre et de les tester très facilement.

## Couche domain

La couche domain est séparée en 3 packages principaux :


### Models

Les models sont des classes contenant les données prêtes pour l'affichage. Elle ne doivent pas contenir d'annotations dépendantes de la couche data. 


### Repositories

Ce packages contient uniquement les interfaces de nos Repositories (Couche Data). Ces interfaces doivent définir le comportement de nos implémentations


### UseCases

Les UseCases contiennent les cas d'utilisation de notre application, c'est notre logique métier. Dans nos usecases nous injections nos repositories (Leurs interfaces). Ces UsesCases doivent surcharger l'opérateur invoke. Ils contiennent donc une seule méthode publique.

```kotlin
class LoginUserUseCase(){
    suspend operator fun invoke(username : String, password : String){ }
}
```

De cette manière nous pouvons alors appeler notre use case comme cela :

```kotlin
// Ici notre use case est injecté dans notre ViewModel
val loginResult = loginUseCase(username, password)
```

La plupart des usecases retournent un Type Resource. Cela permet de gérer facilement les erreurs dans nos ViewModels et d'afficher des messages d'erreurs parlant pour l'utilisateur. Les uses cases ne lèvent donc pas d'exception. Ils doivent catcher ces dernières.


## Couche data

La couche data contient 3 packages. 

### Repositories

Ce package contient l'implémentation des repositories de notre couche domain. Ces repositories sont suffixés de Impl. Dans ces repositories nous injectons nos DAO et notre ApiService. Chaque méthode dans un repoistories peut lever des Exceptions.

### Local

Le package local contient toutes nos sources de données locales. 

#### DAO

Les DAO (Data Access Object) sont des méthodes d'accès à nos données locales. Avec Room, ces classes doivent être annontés de `@Dao`. Toutes les méthodes dans un DAO doivent être suspend pour ne pas bloquer l'interface utilisateur. Exemple de DAO [Dao](./app/src/main/java/fr/group5/magellangpt/data/local/dao/) :
```kotlin
@Dao
interface UserDao {
    @Query("SELECT * FROM userentity WHERE id = :id")
    suspend fun getUser(id: Long): UserEntity
}
```

#### Entities

Les Entities représentent notre modèle de base de données. Elles possèdent toutes les annotations nécéssaires du Framework pour par exemple créer des index et des clés primaires. Exemple d'entité [Entity](./app/src/main/java/fr/group5/magellangpt/data/local/entities)

```kotlin
@Entity
data class UserEntity(
    @PrimaryKey
    val id: Long,
    val name: String,
)
```

### Commons

La couche commons contient toutes les méthodes utilisables à travers toutes les couches de notre application.

#### Extensions

Le package extensions contient toutes les méthodes d'extensions. Exemple de méthode d'extensions. : 
```kotlin
fun Date.toPrettyDate() : String {
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH)
    return dateFormatter.format(this)
}
```

#### Helpers

Les helpers sont des classes permettant de faciliter certaines opérations, comme par exemple récupérer une traduction, afficher une snackbar, réaliser une navigation. [Helpers](./app/src/main/java/fr/thomasbernard03/rickandmorty/commons/helpers/). Tous ces helpers doivent avoir une interface et une implémentation.