# CMU-2019.2020
Projeto de CMU

To list the Dining Places in the DiningOptionsActivity and the Dishes in the DiningPlaceActivity, we should follow this tutorial: https://www.vogella.com/tutorials/AndroidListView/article.html

Something I realized: we store every image as an int which is the reference for the image in the res folder, BUT, perhaps we should consider loading the bitmaps directly into the Dish and DiningOptions classes because this will have to be stored in an external database and we won't be able to fetch the pictures from their int refs.

CHECKLIST:
14-04-2020
X TextBox dos preços deve estar melhorada (só dar para por numeros e por €)
X Imagem de adicionar imagem do prato está desformatada
X Mudar input do construtor do prato para um int e depois ele constroi a string com o €
X Mudar input do preço do prato para o utilizador para ser um numero (teclado numérico).
X Imagem do prato: obrigar o utilizador a selecionar uma imagem.
X Quando o utilizador clica no "Enter" do teclado, o teclado tem de desaparecer
X Opção de mudar username e imagem de perfil.
X Spinner com os campus
X Rating de quem adiciona o prato tem de ser atualizado no sistema

15-04-2020
Localização dos restaurantes
Database e e afins