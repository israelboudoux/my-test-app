# User Cart App

To run the application, issue the following command in the root directory (my-triggerise-test): 
``
mvn exec:java
``
or open the *com.triggerise.App* class in your preferred IDE and run the main method. 

The program will start and request you input some data. 

In the _resources_ directory, there are three files:
1. *pricing-rules.csv* - A CSV file containing all the pricing rules - to add new pricing rules, just add it to this file 
following the CSV formatting and the respective pricing rule's parameters
2. *producs.csv* - A CSV file containing all the available products - to add new products, just add it to this file following
the CSV formatting
3. *pricing-rules.csv* - Contains the pricing rules related to the products. One product can have only one pricing rule 

## Pricing Rules
To create new pricing rules based on the existing ones, just drop it in the *pricing-rules.csv* file and change the 
parameters accordingly you wish.
Currently there are two Pricing Rules: 
1. BuyXGetYPricingRule: allows to apply the rule 'Buy X and get Y products'. The first parameter must be the _X_ value 
(how many of some product the user should buy to enable the promotion) and the _Y_ value that must be greater than _X_ 
and is the quantity of the user will get, in case the promotion applies. e.g.: 10_for_7; BuyXGetYPricingRule; Buy 7 get 10; 7, 10

2. DynamicDiscountPricingRule: allows to apply the rule 'Get a discount of X when buying Y or more'. It is enabled when 
the user buy at least Y units of the product, so the discout is applied in each item of that product. 
e.g.: get_15_when_qty_gte_3; DynamicDiscountPricingRule; 15 discount over each item when quantity is greater than or equal 3; 3, 15.00