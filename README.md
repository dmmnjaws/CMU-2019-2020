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
Formatar imagens de restaurantes de forma a que fiquem quadradas e corretas
Formatar imagem de perfil de forma a que fique sempre redonda
X Corrigir populações duplicadas do estado local
Tempo esperado de espera nas filas dos restaurantes.

NOTAS:
Se fizermos uma interface de chat entre amigos, podemos explorar o lab4 sobre como fazer WIFI Groups.


Gostariamos de focar o feedback nas seguintes questões:

1 - A nossa aplicação assume que todos os pratos são adicionados por utilizadores, ou seja, no inicio da aplicação, só estão presentes os restaurantes e depois cada restaurante terá de popular o seu menu com os pratos disponiveis. (E cada utilizador pode adicionar um prato, dar rating a pratos já existentes, e colocar fotos em pratos já existentes.) Isto está bom como está?

2 - Por enquanto o nosso sistema de queues é limitado, apenas considera a quantidade de gente que está à nossa volta a uma determinada altura independentemente do restaurante, para combater isto tivemos uma ideia e estavamos a pensar implementá-la até para abrir as portas para podermos eventualmente implementar "Meta-Moderation" no futuro, queriamos saber se é uma boa ideia ou se há outra sugestão por parte do professor.

2.1 - Um telemóvel "administrador" terá de estar átivo no restaurante X e este sim calcula a quantidade de dispositivos à sua volta e infere um tempo de espera, o tempo de espera é obtido pelo utilizador fazendo um pedido por sockets a este telemóvel administrador. (Isto implicaria criar um novo projeto de android studio). Concretizando isto, a partir desse telemovel administrador, o dono do restaurante por exemplo poderia também pedir ao servidor os pratos/imagens que as pessoas adicionaram a este restaurante e remover o conteudo não apropriado. Poderia também adicionar ele próprio os pratos ao menu do seu restaurante, para "publicitar".

3 - Coordenadas aparecem corretamente no pc do meu colega e aparecem erradas no meu pc.



