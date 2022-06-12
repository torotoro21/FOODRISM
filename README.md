# FOODRISM APP

## About
* Foodrism is an android app which classifies traditional foods based on pictures. The main objective of this app is to promote traditional Indonesian foods through mobile application. You can explore a variety of traditional foods as well as classifies the food that you take from camera or gallery. Foodrism will give detailed information about the food including history, calories, recipes, how-to-make, and much more. 
* This project was a part of Bangkit 2022 Product-based Capstone Project with selected theme: **Tourism, Creative, and Digital Economy**

## Contributors:
* Team ID: C22-PS026
1. A2223W2035 - Muhamad Nashiruddin Zaki [MD]
2. A2152F1680 - Fathurrohman Abdul Aziz [MD]
3. C2014J1375 - Fadel Faridzky [CC]
4. M7012F1300 - Angga Prihantoro [ML]
5. M7223W2029 - Muhammad Bahaudin Mahmud [ML]
6. M2012F1308 - Muhammad Ilham [ML]

## Branches
| Main | Master |
|--|--|
| ML Models & Dataset | Android Project App |


## Application Usage
* Go to the [`master`](https://github.com/torotoro21/FOODRISM/tree/master) branch and follow the instruction

***
# Implementation
### Machine Learning 
* Collecting dataset by scraping from google
* Splitting dataset into training and testing
* Load pre-trained model "mobilenetV2" and get the architecture using hub.Keraslayer then add more layer
* Training dataset
* Create prediction and testing the result
* Convert keras model into tflite model
* Predicting from tflite model

### Mobile Development 
* Develop application design and user flow
* Implementing Tensorflow Lite for image processing
* Showing information for scanned result
* Implementing login & register users

### Cloud Computing 
* Make an authentication API for the app
* Saving user data in google cloud


### Related Links:
* [Application Prototype](https://www.figma.com/proto/e3NHiaDuk3OKyFhBpaYI8z/Foodrism-Mobile-App) 
* [Demo Video](https://drive.google.com/file/d/1YI4R75VagaEuZwFKltFZOponmBy-LWE1/view)

