# RESTful JAX-RS Application : Gestion securisee (JWT) de donnees de users

## RESTful API : operations disponibles

### Operations associees a la gestion des utilisateurs et l'authentification

<table style="caption-side: top">
<caption>Operations sur les ressources de type "User"</caption>
<tr>
    <th>URI</th>
    <th>Methode</th>
    <th>Auths?</th>
    <th>Operation</th>
</tr>

<tr>
    <td>users/login</td>
    <td>GET</td>
    <td>JWT</td>
    <td>
    Connecte l utilisateur si il a fournit un JWT valide et renvoie ce token ainsi que les info public du user.
    </td>
</tr>
<tr>
    <td>users/login</td>
    <td>POST</td>
    <td>Non</td>
    <td>
    Connecte l'utilisateur si il a fournit une combinaison username-password valide et renvoie un token JWT ainsi que les infos public du user.
    </td>
</tr>
<tr>
    <td>users/me</td>
    <td>GET</td>
    <td>JWT</td>
    <td>
    Renvoie les informations public de l utilisateur si il a fournit un JWT correct.
    </td>
</tr>
<tr>
    <td>users/signup</td>
    <td>POST</td>
    <td>Non</td>
    <td>
    TODO
    Créé un utilisateur et le renvoie ces informations public ainsi que son JWT
    </td>
</tr>
<tr>
    <td>users/customers</td>
    <td>GET</td>
    <td>JWT</td>
    <td>
    TODO
    Renvoie la liste entière des clients.
    </td>
</tr>
<tr>
    <td>users/customers</td>
    <td>POST</td>
    <td>JWT</td>
    <td>
    TODO
    Renvoie la liste des clients en fonction du filtre passé à la requête. Le filtre s'applique sur le nom du client, sa commune ainsi que sa boite postale.
    </td>
</tr>

</table>