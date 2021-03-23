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
    Connecte l utilisateur si il a fournit un JWT valide et renvoie ce token ainsi que ces info public.
    </td>
</tr>
<tr>
    <td>users/login</td>
    <td>POST</td>
    <td>Aucun</td>
    <td>
    Connecte l'utilisateur si il a fournit une combinaison username-password valide et renvoie un token JWT ainsi que ces infos public.
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
    <td>users/register</td>
    <td>POST</td>
    <td>Aucun</td>
    <td>
    Créé une ressource de type "user" et renvoie ces informations public ainsi que son JWT
    </td>
</tr>
<tr>
    <td>users/customers</td>
    <td>GET</td>
    <td>Admin</td>
    <td>
    Renvoie la liste entière des ressources de type "user".
    </td>
</tr>
<tr>
    <td>users/customers</td>
    <td>POST</td>
    <td>Admin</td>
    <td>
    Renvoie la liste des clients en fonction du filtre passé à la requête. Le filtre s'applique sur le nom du client, son prénom, sa commune ainsi que sur le numéro de sa boite postale.
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
    Renvoie les informations publics d'une ressource de type "furniture".
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