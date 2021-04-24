# RESTful JAX-RS Application : Gestion securisee (JWT) de donnees de users

## RESTful API : operations disponibles

### Operations associees a la gestion des utilisateurs et l'authentification

<table style="caption-side: top">
<caption>Operations sur les ressources de type "User"</caption>
<tr>
    <th>URI</th>
    <th>Methode</th>
    <th>Droits requis</th>
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
    <td>users/detail</td>
    <td>GET</td>
    <td>Admin</td>
    <td>
    Renvoie une liste des informations reservees aux admins des ressources de type "user".
    </td>
</tr>
<tr>
    <td>users/detail/search</td>
    <td>POST</td>
    <td>Admin</td>
    <td>
    Renvoie une liste des informations reservees aux admins des ressources de type "user" en fonction de la recherche passée
    à la requête.
    La recherche s'applique sur le nom des users, leurs prénoms, leurs communes ainsi que sur les numéros de leur boites
    postale.
    </td>
</tr>
<tr>
    <td>users/detail/waiting</td>
    <td>GET</td>
    <td>Admin</td>
    <td>
    Renvoie une liste des informations reservees aux admins des ressources de type "user" en attente de confirmation d'inscription.
    </td>
</tr>
<tr>
    <td>users/detail/confirmed</td>
    <td>GET</td>
    <td>Admin</td>
    <td>
    Renvoie une liste des informations reservees aux admins des ressources de type "user" dont l'inscription a été confirmée.
    </td>
</tr>
<tr>
    <td>users/detail/{id}</td>
    <td>GET</td>
    <td>Admin</td>
    <td>
    Renvoie les détails d'un user précis.
    </td>
</tr>
<tr>
    <td>users/validate/{id}</td>
    <td>PATCH</td>
    <td>Admin</td>
    <td>
    Renvoie le user DTO et set le waiting du user précis à false et si la validation est positive il le garde son role si non il set le role à customer.
    </td>
</tr>
</table>

<table style="caption-side: top">
<caption>Operations sur les ressources de type "Furniture"</caption>
<tr>
    <th>URI</th>
    <th>Methode</th>
    <th>Droits requis</th>
    <th>Operation</th>
</tr>

<tr>
    <td>furniture/{id}</td>
    <td>GET</td>
    <td>Aucun</td>
    <td>
    Renvoie les informations publiques d'une ressource de type "furniture".
    (La ressource doit être dans l'état 'disponible à la vente' ou 'vendu')
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
    Renvoie une liste contenant les informations publiques de toutes les ressources de type "furniture" 
    étant dans l'état 'disponible à la vente' ou 'vendu'.
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

<tr>
    <td>furniture/restoration/{id}</td>
    <td>PATCH</td>
    <td>Admin</td>
    <td>
    Modifie l'état d'une ressource de type 'furniture' vers 'in_restoration', et renvoie les informations mises à jour.
    </td>
</tr>
<tr>
    <td>furniture/available/{id}</td>
    <td>PATCH</td>
    <td>Admin</td>
    <td>
    Modifie l'état d'une ressource de type 'furniture' vers 'available_for_sale', et renvoie les informations mises à jour.
    Le body de la requête contient un double 'selling_price'.
    </td>
</tr>
<tr>
    <td>furniture/withdraw/{id}</td>
    <td>PATCH</td>
    <td>Admin</td>
    <td>
    Modifie l'état d'une ressource de type 'furniture' vers 'withdrawn', et renvoie les informations mises à jour.
    </td>
</tr>
<tr>
    <td>furniture/favouritePhoto/{id}</td>
    <td>PATCH</td>
    <td>Admin</td>
    <td>
    Modifie la photo favorite d'une ressource de type 'furniture', et renvoie les informations mises à jour.
    </td>
</tr>

</table>

<table style="caption-side: top">
<caption>Operations sur les ressources de type "Photo"</caption>
<tr>
    <th>URI</th>
    <th>Methode</th>
    <th>Droits requis</th>
    <th>Operation</th>
</tr>
<tr>
    <td>photos/</td>
    <td>POST</td>
    <td>Admin</td>
    <td>
      Ajoute une image a la base de donnée.
    </td>
</tr>
<tr>
    <td>photos/homePage</td>
    <td>GET</td>
    <td>Aucun</td>
    <td>
        Renvoie une liste de toutes les information sur les ressources 
        de type "photo" qui sont visible dans la home page.
    </td>
</tr>
<tr>
    <td>photos/displayFlags/{id}</td>
    <td>PATCH</td>
    <td>Admin</td>
    <td>
        Modifie les flags 'isVisible' et 'isOnHomePage' d'une ressource de type "photo" 
        (Renvoie la ressource modifiée au format json)
    </td>
</tr>
</table>

<table style="caption-side: top">
<caption>Operations sur les ressources de type "Option"</caption>
<tr>
    <th>URI</th>
    <th>Methode</th>
    <th>Droits requis</th>
    <th>Operation</th>
</tr>
<tr>
    <td>options/</td>
    <td>GET</td>
    <td>Admin</td>
    <td>Renvoie une liste de toutes les ressources de type option.</td>
</tr>
<tr>
    <td>options/</td>
    <td>POST</td>
    <td>Admin</td>
    <td>Renvoie l'option crée</td>
</tr>
<tr>
    <td>options/cancel/{id}</td>
    <td>PATCH</td>
    <td>Admin</td>
    <td>
    Annule une ressource de type option et renvoie l'option modifiée. 
    </td>
</tr>
</table>