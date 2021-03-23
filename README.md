# RESTful JAX-RS Application : Gestion securisee (JWT) de donnees de users

## RESTful API : operations disponibles

### Operations associees a la gestion des utilisateurs et l'authentification

<table style="caption-side: top">
<caption>Operations sur les ressources de type "User"</caption>
<tr>
    <th>URI</th>
    <th>Methode</th>
    <th>Droit(s) requit(s)</th>
    <th>Operation</th>
</tr>

<tr>
    <td>users/login</td>
    <td>GET</td>
    <td>JWT</td>
    <td>
    Connecte l utilisateur si il a fournit un JWT valide et renvoie ce token ainsi que ces informations publiques.
    </td>
</tr>
<tr>
    <td>users/login</td>
    <td>POST</td>
    <td>Aucun</td>
    <td>
    Connecte l'utilisateur si il a fournit une combinaison username-password valide et renvoie un token JWT ainsi que ces informations publiques.
    </td>
</tr>
<tr>
    <td>users/me</td>
    <td>GET</td>
    <td>JWT</td>
    <td>
    Renvoie les informations publique de l utilisateur si il a fournit un JWT correct.
    </td>
</tr>
<tr>
    <td>users/register</td>
    <td>POST</td>
    <td>Aucun</td>
    <td>
    Créé une ressource de type "user" et renvoie ces informations publique ainsi que son JWT
    </td>
</tr>
<tr>
    <td>users/customers</td>
    <td>GET</td>
    <td>Admin</td>
    <td>
    Renvoie une liste des informations reservees aux admins des ressources de type "user".
    </td>
</tr>
<tr>
    <td>users/customers</td>
    <td>POST</td>
    <td>Admin</td>
    <td>
    Renvoie une liste des informations reservees aux admins des ressources de type "user" en fonction du filtre passé
    à la requête.
    Le filtre s'applique sur le nom des users, leurs prénoms, leurs communes ainsi que sur les numéros de leur boites
    postale.
    </td>
</tr>

</table>

<table style="caption-side: top">
<caption>Operations sur les ressources de type "Furniture"</caption>
<tr>
    <th>URI</th>
    <th>Methode</th>
    <th>Droit(s) requit(s)</th>
    <th>Operation</th>
</tr>

<tr>
    <td>furniture/{id}</td>
    <td>GET</td>
    <td>Aucun</td>
    <td>
    Renvoie les informations publiques d'une ressource de type "furniture".
    </td>
</tr>

<tr>
    <td>furniture/detail/{id}</td>
    <td>GET</td>
    <td>Admin</td>
    <td>
    Renvoie les informations reservees aux admins d'une ressource de type "furniture".
    </td>
</tr>

<tr>
    <td>furniture/</td>
    <td>GET</td>
    <td>Aucun</td>
    <td>
    Renvoie une liste contenant les informations publiques de toutes les ressources de type "furniture".
    </td>
</tr>

<tr>
    <td>furniture/detail</td>
    <td>GET</td>
    <td>Admin</td>
    <td>
    Renvoie une liste contenant les informations reservees aux admins de toutes les ressources de type "furniture".
    </td>
</tr>

</table>