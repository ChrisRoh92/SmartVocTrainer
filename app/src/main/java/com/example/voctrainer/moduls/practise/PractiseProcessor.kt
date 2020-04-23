package com.example.voctrainer.moduls.practise


import com.example.voctrainer.backend.database.entities.Setting
import com.example.voctrainer.backend.database.entities.Voc
import kotlin.random.Random

object PractiseProcessor
{

    fun getVocsToPractise(vocs:List<Voc>, setting: Setting):ArrayList<Voc>
    {
        var items:ArrayList<Voc> = ArrayList()
        for (i in vocs)
        {
            // ungelernte und angefangen gelernte
            if(setting.settingsMode == 1)
            {
                if(i.status <= 1)
                    items.add(i)
            }
            // Nur ungelernte
            else
            {
                items.add(i)
            }
        }


        var resultItems:ArrayList<Voc> = ArrayList()
        // Lokale Funktion um zu prüfen ob Vokabel schon enthalten...
        fun checkIfItemExist(item:Voc):Boolean
        {
            return resultItems.contains(item)
        }

        // 1. Fall: Mehr Vokabeln, als es die Settings wollen:
        if(setting.itemCount > items.size)
        {
            while(resultItems.size != setting.itemCount)
            {
                if(checkIfItemExist(resultItems[Random.nextInt(0,resultItems.lastIndex)]))
                {
                    resultItems.add(resultItems[Random.nextInt(0,resultItems.lastIndex)])
                }
            }
        }
        // 2. Fall: Genau so viele Vokabeln wie Settings möchte
        else if (setting.itemCount == items.size)
        {
            resultItems.addAll(items)
        }
        // 3. Fall: Weniger Voks wie gewollt
        else
        {

        }


        return resultItems


    }




}