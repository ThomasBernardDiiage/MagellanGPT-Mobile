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
