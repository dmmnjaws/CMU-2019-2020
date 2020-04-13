# CMU-2019.2020
Projeto de CMU

To list the Dining Places in the DiningOptionsActivity and the Dishes in the DiningPlaceActivity, we should follow this tutorial: https://www.vogella.com/tutorials/AndroidListView/article.html

Something I realized: we store every image as an int which is the reference for the image in the res folder, BUT, perhaps we should consider loading the bitmaps directly into the Dish and DiningOptions classes because this will have to be stored in an external database and we won't be able to fetch the pictures from their int refs.

-> TextBox dos preços deve estar melhorada (só dar para por numeros e por €)
-> Imagem de adicionar imagem do prato está desformatada