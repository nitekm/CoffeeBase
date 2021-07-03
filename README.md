# CoffeeBase

## Project info

CoffeeBase is an android app client for CoffeeBase API.
App purpose is to send and receive requests from API using [Retrofit](https://square.github.io/retrofit/) and 
present them in elegant form, using some of Android features like Card View and Recycler View.  

Please consider that I'm mostly backend developer and designing UI is not my strongest point.  
Also android app does not use all of API functionalities yet.

For server (better) side of application please refer to [CoffeeBase API](https://github.com/nitekm/CoffeeBaseApi)

## Technology Stack
* Java 11
* Android SDK
* Retrofit 2

## Application Presentation

### Home
This is app home page. From here we can navigate to:
* Add new coffee
* My CoffeeBase
* Favourites
* About (which basically says that I made this :)  
  
![Home](https://user-images.githubusercontent.com/72076364/124353310-0cf17f80-dc06-11eb-90d2-9c04cd4d03eb.PNG)
  
### Add new coffee
Activity for adding new coffee. We can provide info such as: name (required), origin, roaster, our personal 
rating 1-5 and even coffee bag image from web.  
![Add new Coffee](https://user-images.githubusercontent.com/72076364/124353305-06630800-dc06-11eb-9bfd-7a10f329ec0a.PNG)

### My CoffeeBase
Here all our coffees are presented with image (if we added one), name and rating. We can also sort coffees
A-Z, Z-A or by rating - ascending and descending.  
![My CoffeeBase](https://user-images.githubusercontent.com/72076364/124353448-e4b65080-dc06-11eb-976d-5dd24c4037fa.PNG)  

Clicking an item will show us coffee preview. Here we can use trash can button to delete coffee and 'Add to Favourites'
button to, well - add it to our favourites  
![Cofee View](https://user-images.githubusercontent.com/72076364/124353306-07943500-dc06-11eb-99e7-1472237d35a0.PNG)  

### Favourites
Here we can take a look at our favourite coffees  
![Favourites](https://user-images.githubusercontent.com/72076364/124353308-0b27bc00-dc06-11eb-9f56-0e6dc5bbff51.PNG)  
And also preview single item  
![Favourite single coffee](https://user-images.githubusercontent.com/72076364/124353307-095df880-dc06-11eb-8cad-a21a0ffd99fa.PNG)  
When coffee is in favourites, button changes to 'Remove from Favourites'

### About
Yup, that's all  
![About](https://user-images.githubusercontent.com/72076364/124353303-0400ae00-dc06-11eb-8910-1cb3f59f845e.PNG)
