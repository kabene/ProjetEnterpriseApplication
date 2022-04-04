# RESTful JAX-RS Application : Satcho Furniture
## You are the visitor \#
![Visitor Count](https://profile-counter.glitch.me/{yourUsername}/count.svg)
## RESTful API : operations disponibles

<table style="caption-side: top">
<caption>Operations sur les ressources de type "User"</caption>
<tr>
    <th>URI</th>
    <th>Methode</th>
    <th>Droits requis</th>
    <th>Autorise la prise de contrôle</th>
    <th>Operation</th>
</tr>
<tr>
    <td>users/login</td>
    <td>GET</td>
    <td>JWT</td>
    <td>Non</td>
    <td>
    Connecte l utilisateur si il a fournit un JWT valide et renvoie ce token ainsi que ces informations publiques.
    </td>
</tr>
<tr>
    <td>users/login</td>
    <td>POST</td>
    <td>Aucun</td>
    <td>Non</td>
    <td>
    Connecte l'utilisateur si il a fournit une combinaison username-password valide et renvoie un token JWT ainsi que ces informations publiques.
    </td>
</tr>
<tr>
    <td>users/me</td>
    <td>GET</td>
    <td>JWT</td>
    <td>Oui</td>
    <td>
    Renvoie les informations publique de l utilisateur si il a fournit un JWT correct.
    </td>
</tr>
<tr>
    <td>users/register</td>
    <td>POST</td>
    <td>Aucun</td>
    <td>Non</td>
    <td>
    Créé une ressource de type "user" et renvoie ces informations publique ainsi que son JWT
    </td>
</tr>
<tr>
    <td>users/detail</td>
    <td>GET</td>
    <td>Admin</td>
    <td>Non</td>
    <td>
    Renvoie une liste des informations reservees aux admins des ressources de type "user".
    </td>
</tr>
<tr>
    <td>users/detail/waiting</td>
    <td>GET</td>
    <td>Admin</td>
    <td>Non</td>
    <td>
    Renvoie une liste des informations reservees aux admins des ressources de type "user" en attente de confirmation d'inscription.
    </td>
</tr>
<tr>
    <td>users/detail/confirmed</td>
    <td>GET</td>
    <td>Admin</td>
    <td>Non</td>
    <td>
    Renvoie une liste des informations reservees aux admins des ressources de type "user" dont l'inscription a été confirmée.
    </td>
</tr>
<tr>
    <td>users/detail/{id}</td>
    <td>GET</td>
    <td>Admin</td>
    <td>Non</td>
    <td>
    Renvoie les détails d'un user précis.
    </td>
</tr>
<tr>
    <td>users/validate/{id}</td>
    <td>PATCH</td>
    <td>Admin</td>
    <td>Non</td>
    <td>
    Renvoie le user DTO et set le waiting du user précis à false et si la validation est positive il le garde son role si non il set le role à customer.
    </td>
</tr>
<tr>
    <td>users/takeover/{id}</td>
    <td>GET</td>
    <td>Admin</td>
    <td>Non</td>
    <td>
    Renvoie un JWT de prise de contrôle, et une ressource de type "user" contenant les informations de l'utilisateur contrôlé.
    </td>
</tr>
</table>

<table style="caption-side: top">
<caption>Operations sur les ressources de type "Furniture"</caption>
<tr>
    <th>URI</th>
    <th>Methode</th>
    <th>Droits requis</th>
    <th>Autorise la prise de contrôle</th>
    <th>Operation</th>
</tr>
<tr>
    <td>furniture/{id}</td>
    <td>GET</td>
    <td>Aucun</td>
    <td>Non</td>
    <td>
    Renvoie les informations publiques d'une ressource de type "furniture".
    (La ressource doit être dans l'état 'disponible à la vente' ou 'vendu')
    </td>
</tr>
<tr>
    <td>furniture/detail/{id}</td>
    <td>GET</td>
    <td>Admin</td>
    <td>Non</td>
    <td>
    Renvoie les informations reservees aux admins d'une ressource de type "furniture".
    </td>
</tr>
<tr>
    <td>furniture/</td>
    <td>GET</td>
    <td>Aucun</td>
    <td>Non</td>
    <td>
    Renvoie une liste contenant les informations publiques de toutes les ressources de type "furniture" 
    étant dans l'état 'disponible à la vente' ou 'vendu'.
    </td>
</tr>
<tr>
    <td>furniture/detail</td>
    <td>GET</td>
    <td>Admin</td>
    <td>Non</td>
    <td>
    Renvoie une liste contenant les informations reservees aux admins de toutes les ressources de type "furniture".
    </td>
</tr>
<tr>
    <td>furniture/accepted/{id}</td>
    <td>PATCH</td>
    <td>Admin</td>
    <td>Non</td>
    <td>
    Modifie l'état d'une ressource de type 'furniture' vers 'available', et renvoie les informations mises à jour.
    </td>
</tr>
<tr>
    <td>furniture/refused/{id}</td>
    <td>PATCH</td>
    <td>Admin</td>
    <td>Non</td>
    <td>
    Modifie l'état d'une ressource de type 'furniture' vers 'refused', et renvoie les informations mises à jour.
    </td>
</tr>
<tr>
    <td>furniture/restoration/{id}</td>
    <td>PATCH</td>
    <td>Admin</td>
    <td>Non</td>
    <td>
    Modifie l'état d'une ressource de type 'furniture' vers 'in_restoration', et renvoie les informations mises à jour.
    </td>
</tr>
<tr>
    <td>furniture/available/{id}</td>
    <td>PATCH</td>
    <td>Admin</td>
    <td>Non</td>
    <td>
    Modifie l'état d'une ressource de type 'furniture' vers 'available_for_sale', et renvoie les informations mises à jour.
    Le body de la requête contient un double 'selling_price'.
    </td>
</tr>
<tr>
    <td>furniture/withdraw/{id}</td>
    <td>PATCH</td>
    <td>Admin</td>
    <td>Non</td>
    <td>
    Modifie l'état d'une ressource de type 'furniture' vers 'withdrawn', et renvoie les informations mises à jour.
    </td>
</tr>
<tr>
    <td>furniture/sold/{id}</td>
    <td>PATCH</td>
    <td>Admin</td>
    <td>Non</td>
    <td>
    Modifie l'état d'une ressource de type 'furniture' vers 'sold', et renvoie les informations mises à jour. 
    Le champ 'specialSalePrice' est optionnel dans le body de la requête.
    </td>
</tr>
<tr>
    <td>furniture/favouritePhoto/{id}</td>
    <td>PATCH</td>
    <td>Admin</td>
    <td>Non</td>
    <td>
    Modifie la photo favorite d'une ressource de type 'furniture', et renvoie les informations mises à jour.
    </td>
</tr>
<tr>
    <td>furniture/infos/{id}</td>
    <td>PATCH</td>
    <td>Admin</td>
    <td>Non</td>
    <td>
    Modifie les informations (description / type id / prix de vente) d'une ressource de type furniture, et renvoie les informations mises à jour.
    </td>
</tr>
</table>

<table style="caption-side: top">
<caption>Operations sur les ressources de type "Photo"</caption>
<tr>
    <th>URI</th>
    <th>Methode</th>
    <th>Droits requis</th>
    <th>Autorise la prise de contrôle</th>
    <th>Operation</th>
</tr>
<tr>
    <td>photos/</td>
    <td>POST</td>
    <td>Admin</td>
    <td>Non</td>
    <td>
      Ajoute une image a la base de donnée.
    </td>
</tr>
<tr>
    <td>photos/homePage</td>
    <td>GET</td>
    <td>Aucun</td>
    <td>Non</td>
    <td>
        Renvoie une liste de toutes les information sur les ressources 
        de type "photo" qui sont visible dans la home page.
    </td>
</tr>
<tr>
    <td>photos/displayFlags/{id}</td>
    <td>PATCH</td>
    <td>Admin</td>
    <td>Non</td>
    <td>
        Modifie les flags 'isVisible' et 'isOnHomePage' d'une ressource de type "photo" 
        (Renvoie la ressource modifiée au format json)
    </td>
</tr>
<tr>
    <td>photos/favourite/{furnitureId}</td>
    <td>GET</td>
    <td>Aucun</td>
    <td>Non</td>
    <td>
        Renvoie la ressource de type "photo" référencée comme photo préférée pour un meuble particulier
    </td>
</tr>
<tr>
    <td>photos/byFurniture/{furnitureId}</td>
    <td>GET</td>
    <td>Aucun</td>
    <td>Non</td>
    <td>
        Renvoie toutes les ressources de type "photo" référencant une certaine furnitureId
        (ne renvoie que les photos visibles)
    </td>
</tr>
<tr>
    <td>photos/byFurniture/all/{furnitureId}</td>
    <td>GET</td>
    <td>Admin</td>
    <td>Non</td>
    <td>
        Renvoie toutes les ressources de type "photo" référencant une certaine furnitureId
        (sans exception)
    </td>
</tr>
<tr>
    <td>photos/byFurniture/request/{furnitureId}</td>
    <td>GET</td>
    <td>JWT</td>
    <td>Oui</td>
    <td>
        Renvoie toutes les ressources de type "photo" liées à une demande de visite et 
        référencant une certaine furnitureId 
    </td>
</tr>
</table>

<table style="caption-side: top">
<caption>Operations sur les ressources de type "FurnitureType"</caption>
<tr>
    <th>URI</th>
    <th>Methode</th>
    <th>Droits requis</th>
    <th>Autorise la prise de contrôle</th>
    <th>Operation</th>
</tr>
<tr>
    <td>furnitureTypes/</td>
    <td>GET</td>
    <td>Aucun</td>
    <td>Non</td>
    <td>Renvoie une liste de touts les types de meubles.</td>
</tr>
</table>

<table style="caption-side: top">
<caption>Operations sur les ressources de type "Option"</caption>
<tr>
    <th>URI</th>
    <th>Methode</th>
    <th>Droits requis</th>
    <th>Autorise la prise de contrôle</th>
    <th>Operation</th>
</tr>
<tr>
    <td>options/</td>
    <td>GET</td>
    <td>JWT</td>
    <td>Non</td>
    <td>Renvoie une liste de toutes les ressources de type option.</td>
</tr>
<tr>
    <td>options/me</td>
    <td>GET</td>
    <td>JWT</td>
    <td>oui</td>
    <td>Renvoie une liste de toutes les ressources de type option mises par l'utilisateur.</td>
</tr>
<tr>
    <td>options/</td>
    <td>POST</td>
    <td>JWT</td>
    <td>Oui</td>
    <td>Crée une ressource de type option et la renvoie.</td>
</tr>
<tr>
    <td>options/cancel/{id}</td>
    <td>PATCH</td>
    <td>JWT</td>
    <td>Oui</td>
    <td>
    Annule une ressource de type option et renvoie l'option modifiée. 
    </td>
</tr>
</table>

<table style="caption-side: top">
<caption>Operations sur les ressources de type "Request for visit"</caption>
<tr>
    <th>URI</th>
    <th>Methode</th>
    <th>Droits requis</th>
    <th>Autorise la prise de contrôle</th>
    <th>Operation</th>
</tr>
<tr>
    <td>requestForVisit/</td>
    <td>GET</td>
    <td>Admin</td>
    <td>Non</td>
    <td>Renvoie une liste de toutes les ressources de type request_for_visit.</td>
</tr>

<tr>
    <td>requestForVisit/cancel/{id}</td>
    <td>PATCH</td>
    <td>Admin</td>
    <td>Non</td>
    <td>Annule une ressource de type request_for_visit et la renvoie.</td>
</tr>

<tr>
    <td>requestForVisit/confirm/{id}</td>
    <td>PATCH</td>
    <td>Admin</td>
    <td>Non</td>
    <td>Accepte une ressource de type request_for_visit et la renvoie.</td>
</tr>

<tr>
    <td>requestForVisit/me</td>
    <td>GET</td>
    <td>JWT</td>
    <td>Oui</td>
    <td>Renvoie une liste de toutes les ressources de type request_for_visit appartenant à l'utilisateur.</td>
</tr>
</table>
