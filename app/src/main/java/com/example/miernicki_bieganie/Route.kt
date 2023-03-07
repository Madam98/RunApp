package com.example.miernicki_bieganie

import java.util.ArrayList
import java.util.HashMap

object Route{

    val route: MutableList<Item> = ArrayList()
    val route_map: MutableMap<String, Item> = HashMap()
//https://www.poznanskieklimaty.pl/7-najlepszych-miejsc-do-biegania-w-poznaniu/
    init {
    addItem(Item("1", "Ścieżka Zdrowia", "Ścieżka zdrowia (długość około 1,6km)\n" +
            "Na całej długości ścieżki rozstawianych jest 15 stacji ćwiczebnych (płotki, drążki ...).\n" +
            "\n" +
            "Początek ścieżki zdrowia jest przy zejściu na plażę 343/3 przed ośrodkiem PROMYK. Dalej przemieszczamy się w kierunku hotelu Senator i z powrotem tworząc pętlę."))
    addItem(Item("2", "Kołobrzeg-Budzistowo-Obroty ", "START: ul. Bolesława Krzywoustego (NETTO)\n" +
            "\n" +
            "Nawierzchnia:  chodnik, płyta betonowa, piaszczysta utwardzona,\n" +
            "\n" +
            "(długość 7km w jedną stronę) \n" +
            "\n" +
            "(około 0,8km) – przebiegamy przez drogę nr.163 (przejście dla pieszych) i chodnikiem biegniemy do Budzistowa.\n" +
            "\n" +
            "(około 2,5km) – Koniec miejscowości Budzistowo (dalej płytami biegniemy do Nowego Miasta).\n" +
            "\n" +
            "(około 4,3km) – zaczyna się las (po lewej stronie).\n" +
            "\n" +
            "(około 5,0km) – biegniemy w lasie.\n" +
            "\n" +
            "(około 5,7km) – skrzyżowanie (skręcamy w LEWO).\n" +
            "\n" +
            "(około 6,2km) – wybiegamy z lasu.\n" +
            "\n" +
            "(około 7,0km) – dobiegamy do drogi głównej w miejscowości Obroty i robimy nawrót."))
}

    private fun addItem(item: Item) {
        route.add(item)
        route_map.put(item.id, item)
    }

    data class Item(val id: String, val content: String, val details: String) {
        override fun toString(): String = content
    }
}