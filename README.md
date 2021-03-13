# TMS (Tiedä Mitä Syöt) / Know What You Eat

This is a school project for a mobile application development course. The main goal of the project is to practice mobile app development using object-oriented programming, using Java as the programming language and Android Studio as the IDE.

The application consists of three main portions: Koti, Profiili, and Ateriat. You can navigate through the activities using the bottom navigation bar. Data and singletons are stored in the user's SharedPreferences folder. 

# Koti
The Koti portion displays the user's information and his or her goal progression. If the user surpasses the daily goals, the progress bar will turn red and an indicator text is displayed on the screen.

You can also navigate to the TiedotMeista activity using the info button on the top right corner, which displays the names of the developers and also describes what the app is all about.
<p>
  <img src="https://github.com/AOskari/TMS/blob/master/images/tms_main.jpg" height="550" width="250">
  <img src="https://github.com/AOskari/TMS/blob/master/images/tms_tietoameista.jpg" height="550" width="250">
</p>

# Ateriat
The Ateriat portion displays the planned meals according to the selected date. The list which contains all the meals is sorted according to their time. Using the buttons set on the meals, you can either check the information, copy, delete, edit or set the meal as eaten.
By pressing the plus button on the top right corner, you can open the Ateria activity. Here, you can plan your meals and set the wanted date and time for the meal. By pressing the plus button on the bottom, you can open the Haku activity. 

In the Haku activity, you can search for groceries and add the wanted amount to the meal you are planning. The groceries are fetched from Fineli's API (https://fineli.fi/fineli/api/v1/foods?q=).
<p>
  <img src="https://github.com/AOskari/TMS/blob/master/images/tms_ateriat.jpg" height="550" width="250">
  <img src="https://github.com/AOskari/TMS/blob/master/images/tms_ateria.jpg" height="550" width="250">
  <img src="https://github.com/AOskari/TMS/blob/master/images/tms_haku.jpg" height="550" width="250">
</p>


There are also a few popups, which are either used for choosing a date, or examining the information of the groceries or meals.
<p>
  <img src="https://github.com/AOskari/TMS/blob/master/images/tms_kalenteri.jpg" height="550" width="250">
  <img src="https://github.com/AOskari/TMS/blob/master/images/tms_tiedot.jpg" height="550" width="250">
  <img src="https://github.com/AOskari/TMS/blob/master/images/tms_ateriainfo.jpg" height="550" width="250">
</p>
<br/>

# Profiili
The Profiili portion consists of two activities: Profiili and Asetukset. The profiili activity displays the user's information and goals. It also displays a GraphView, which shows the user's weight progression. 

The Asetukset activity is used for setting the user's information. In this case, the name, weight, height and the chosen goals. Kalories, protein and carbohydrates can be set as goals.
<p>
  <img src="https://github.com/AOskari/TMS/blob/master/images/tms_profiili.jpg" height="550" width="250">
  <img src="https://github.com/AOskari/TMS/blob/master/images/tms_asetukset.jpg" height="550" width="250">
</p>
