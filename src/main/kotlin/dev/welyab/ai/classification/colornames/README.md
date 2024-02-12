# Color Classificar
This is a study over a neural network used to classify color name based on RBG code values
___

### Color classes

* WHITE
* GRAY
* BLACK
* RED
* GREEN
* BLUE
* ORANGE
* PINK
* YELLOW
* BROWN
* PURPLE
* BEIGE

### Color dataset

A CSV file with RGB codes and its color name: [color_dataset.csv](./../../../../../../resources/dev/welyab/ai/classification/colornames/color_dataset.csv)

Sample dataset CSV content:

```text
red,green,blue,name
247,247,251,white
166,254,152,green
128,226,252,blue
```

Sample RGB colors for red:

<p align="center">
  <img src="./../../../../../../resources/dev/welyab/ai/classification/colornames/sample_images/red.png">
</p>

## Predicted color names

<table>
<tr>
<td><img src="./../../../../../../resources/dev/welyab/ai/classification/colornames/predicted/BLUE_24.png"></td>
<td><img src="./../../../../../../resources/dev/welyab/ai/classification/colornames/predicted/RED_13.png"></td>
<td><img src="./../../../../../../resources/dev/welyab/ai/classification/colornames/predicted/BLACK_11.png"></td>
<td><img src="./../../../../../../resources/dev/welyab/ai/classification/colornames/predicted/YELLOW_29.png"></td>
</tr>
<tr>
<td><img src="./../../../../../../resources/dev/welyab/ai/classification/colornames/predicted/GREEN_19.png"></td>
<td><img src="./../../../../../../resources/dev/welyab/ai/classification/colornames/predicted/PINK_34.png"></td>
<td><img src="./../../../../../../resources/dev/welyab/ai/classification/colornames/predicted/PURPLE_41.png"></td>
<td><img src="./../../../../../../resources/dev/welyab/ai/classification/colornames/predicted/WHITE_3.png"></td>
</tr>
</table>

### Extras

There are a small HTTP API implemented to classify colors over HTTP requests. It is used in personal project to indetify color name with RGB sensor and Arduino board. More in this blog post (in portuguese): 