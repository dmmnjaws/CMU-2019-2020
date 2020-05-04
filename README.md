# CMU-2019.2020
Projeto de CMU

To list the Dining Places in the DiningOptionsActivity and the Dishes in the DiningPlaceActivity, we should follow this tutorial: https://www.vogella.com/tutorials/AndroidListView/article.html

Something I realized: we store every image as an int which is the reference for the image in the res folder, BUT, perhaps we should consider loading the bitmaps directly into the Dish and DiningOptions classes because this will have to be stored in an external database and we won't be able to fetch the pictures from their int refs.

CHECKLIST:
X TextBox dos preços deve estar melhorada (só dar para por numeros e por €)
X Imagem de adicionar imagem do prato está desformatada
X Mudar input do construtor do prato para um int e depois ele constroi a string com o €
X Mudar input do preço do prato para o utilizador para ser um numero (teclado numérico).
X Imagem do prato: obrigar o utilizador a selecionar uma imagem.
X Quando o utilizador clica no "Enter" do teclado, o teclado tem de desaparecer
X Opção de mudar username e imagem de perfil.
X Spinner com os campus
X Rating de quem adiciona o prato tem de ser atualizado no sistema
X Localização dos restaurantes
X Database e e afins
X Formatar imagens de restaurantes de forma a que fiquem quadradas e corretas
Formatar imagem de perfil de forma a que fique sempre redonda
X Corrigir populações duplicadas do estado local
XTempo esperado de espera nas filas dos restaurantes.

CHECKLIST DEPOIS DO FEEDBACK:

X - Ratings dos restaurantes
X Histogramas com ratings para os pratos e restaurantes
- Cache (Oh boy...)
X Corrigir Wi-Fi direct com os bluetooth beacons (Oh boy... pt2)
X - Waiting Time na DiningOptionsActivity
X - Walking Time na DiniingOptionsActivity (Ainda não funciona)
X - Correções ao mapa: mapa mostra a "pequeno" onde fica o restaurante, e clicamos neste mapa para obter as "direções"
X - Corrigir spinner do campus que não está a funcionar.
X - Detetar se utilizador não está perto de nenhum dos campuses.
X - Não há thumbnails obrigatórias nos dishes.
X - Corrigir BUG: Sempre que um cliente entra na página de um prato, o rating é "readicionado" ao server. Embora seja idempotente, está a gastar recursos ao fazer pedidos desnecessários.
 - Autorização: negar acesso a users já logados (nao pode estar logado em dois telemoveis)
X - Dishes can have only one category
X - Scratch "Gluten-Free" and add "Vegetarian"
 - Pictogramas