package com.azan.prayertimes.data

data class Kalima(
    val number: Int,
    val title: String,
    val arabic: String,
    val transliteration: String,
    val englishMeaning: String
)

data class NamazStep(
    val stepNumber: Int,
    val stepName: String,
    val arabicRecitation: String,
    val transliteration: String,
    val explanation: String
)

data class PrayerRakat(
    val prayerName: String,
    val sunnatMuakkadaBefore: Int,
    val farz: Int,
    val sunnatMuakkadaAfter: Int,
    val nafl: Int,
    val witr: Int,
    val total: Int
)

data class DuaItem(
    val title: String,
    val occasion: String,
    val arabic: String,
    val transliteration: String,
    val translation: String
)

data class TasbihPreset(
    val id: String,
    val title: String,
    val arabic: String,
    val targetCount: Int,
    val description: String
)

object IslamicData {

    val KALIMAS = listOf(
        Kalima(
            number = 1,
            title = "First Kalima (Tayyibah - Purity)",
            arabic = "لَا إِلٰهَ إِلَّا اللهُ مُحَمَّدٌ رَسُولُ اللهِ",
            transliteration = "Laaa Ilaaha Illa-llaahu Muhammadu-r-Rasoolu-llaah",
            englishMeaning = "There is no God but Allah, Muhammad (peace be upon him) is the Messenger of Allah."
        ),
        Kalima(
            number = 2,
            title = "Second Kalima (Shahadah - Testimony)",
            arabic = "أَشْهَدُ أَنْ لَا إِلٰهَ إِلَّا اللهُ وَحْدَهُ لَا شَرِيكَ لَهُ وَأَشْهَدُ أَنَّ مُحَمَّدًا عَبْدُهُ وَرَسُولُهُ",
            transliteration = "Ash-hadu Al-laaa Ilaaha Illa-llaahu Wahdahoo Laa Shareeka Lahoo Wa-Ash-hadu Anna Muhammadan 'Abduhoo Wa Rasooluh",
            englishMeaning = "I bear witness that there is no God but Allah, He is One, without partner, and I bear witness that Muhammad is His servant and His Messenger."
        ),
        Kalima(
            number = 3,
            title = "Third Kalima (Tamjeed - Glory of Allah)",
            arabic = "سُبْحَانَ اللهِ وَالْحَمْدُ لِلّٰهِ وَلَا إِلٰهَ إِلَّا اللهُ وَاللهُ أَكْبَرُ وَلَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللهِ الْعَلِيِّ الْعَظِيمِ",
            transliteration = "Subhaan-Allaahi Wal-Hamdu Lillaahi Wa Laaa Ilaaha Illa-llaahu Wallaahu Akbar, Wa Laa Hawla Wa Laa Quwwata Illaa Billaahi-l 'Aliyyi-l 'Azeem",
            englishMeaning = "Glory be to Allah, and all praise is for Allah, and there is no God but Allah, and Allah is the Greatest. And there is no power and no might except from Allah, the Most High, the Most Great."
        ),
        Kalima(
            number = 4,
            title = "Fourth Kalima (Tawheed - Oneness of Allah)",
            arabic = "لَا إِلٰهَ إِلَّا اللهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ يُحْيِي وَيُمِيتُ وَهُوَ حَيٌّ لَا يَمُوتُ أَبَدًا أَبَدًا، ذُو الْجَلَالِ وَالْإِكْرَامِ، بِيَدِهِ الْخَيْرُ، وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ",
            transliteration = "Laaa Ilaaha Illa-llaahu Wahdahoo Laa Shareeka Lahoo, Lahu-l Mulku Wa Lahu-l Hamdu Yuhyee Wa Yumeetu Wa Huwa Hayyu-l Laa Yamootu Abadan Abadaa, Dhu-l Jalaali Wal-Ikraam, Biyadihi-l Khayr, Wa Huwa 'Alaa Kulli Shay'in Qadeer",
            englishMeaning = "There is no God but Allah, He is Alone and has no partner. To Him belongs the Kingdom and to Him is all praise. He gives life and causes death, and He is Alive, He never dies. Possessor of Majesty and Honor, in His hand is all good, and He has power over everything."
        ),
        Kalima(
            number = 5,
            title = "Fifth Kalima (Astaghfar - Seeking Forgiveness)",
            arabic = "أَسْتَغْفِرُ اللهَ رَبِّي مِنْ كُلِّ ذَنْبٍ أَذْنَبْتُهُ عَمْدًا أَوْ خَطَأً، سِرًّا أَوْ عَلَانِيَةً، وَأَتُوبُ إِلَيْهِ مِنَ الذَّنْبِ الَّذِي أَعْلَمُ وَمِنَ الذَّنْبِ الَّذِي لَا أَعْلَمُ، إِنَّكَ أَنْتَ عَلَّامُ الْغُيُوبِ وَسَتَّارُ الْعُيُوبِ وَغَفَّارُ الذُّنُوبِ، وَلَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللهِ الْعَلِيِّ الْعَظِيمِ",
            transliteration = "Astaghfiru-llaaha Rabbee Min Kulli Dhambin Adhnabtuhoo 'Amadan Aw Khata'an, Sirran Aw 'Alaaniyyatan, Wa Atoobu Ilayhi Mina-dh Dhambi-l Ladhee A'lamu Wa Mina-dh Dhambi-l Ladhee Laaa A'lam, Innaka Anta 'Allaamu-l Ghuyoobi Wa Sattaaru-l 'Uyoobi Wa Ghaffaaru-dh Dhunoob, Wa Laa Hawla Wa Laa Quwwata Illaa Billaahi-l 'Aliyyi-l 'Azeem",
            englishMeaning = "I seek forgiveness from Allah, my Lord, for every sin I committed deliberately or by error, secretly or openly, and I turn to Him in repentance from the sin that I know and from the sin that I do not know. Truly You are the Knower of the unseen and the Concealer of faults and the Forgiver of sins; and there is no power and no might except from Allah, the Most High, the Most Great."
        ),
        Kalima(
            number = 6,
            title = "Sixth Kalima (Radde Kufr - Refutation of Disbelief)",
            arabic = "اَللّٰهُمَّ إِنِّي أَعُوذُ بِكَ مِنْ أَنْ أُشْرِكَ بِكَ شَيْئًا وَأَنَا أَعْلَمُ بِهِ، وَأَسْتَغْفِرُكَ لِمَا لَا أَعْلَمُ بِهِ، تُبْتُ عَنْهُ وَتَبَرَّأْتُ مِنَ الْكُفْرِ وَالشِّرْكِ وَالْكِذْبِ وَالْغِيبَةِ وَالْبِدْعَةِ وَالنَّمِيمَةِ وَالْفَوَاحِشِ وَالْبُهْتَانِ وَالْمَعَاصِي كُلِّهَا، وَأَسْلَمْتُ وَأَقُولُ: لَا إِلٰهَ إِلَّا اللهُ مُحَمَّدٌ رَسُولُ اللهِ",
            transliteration = "Allaahumma Inneee A'oodhu Bika Min An Ushrika Bika Shay'an Wa-Anaa A'lamu Bihee, Wa Astaghfiruka Limaa Laaa A'lamu Bihee, Tubtu 'Anhu Wa Tabarraa'tu Mina-l Kufri Wash-Shirki Wal-Kidhbi Wal-Gheebati Wal-Bid'ati Wan-Nameemati Wal-Fawaahishi Wal-Buhtaani Wal-Ma'aasee Kullihaa, Wa Aslamtu Wa Aqoolu: Laaa Ilaaha Illa-llaahu Muhammadu-r-Rasoolu-llaah",
            englishMeaning = "O Allah! I seek refuge in You from knowingly associating any partner with You, and I seek Your forgiveness for that which I do not know. I repent from it and reject disbelief, polytheism, falsehood, backbiting, innovation, tale-bearing, obscenity, slander, and all acts of disobedience. I have submitted and I declare: There is no God but Allah, Muhammad is the Messenger of Allah."
        )
    )

    val DUAS = listOf(
        DuaItem(
            title = "Du'a After Azan",
            occasion = "Recited immediately after listening to the Call to Prayer",
            arabic = "اَللّٰهُمَّ رَبَّ هٰذِهِ الدَّعْوَةِ التَّامَّةِ، وَالصَّلَاةِ الْقَائِمَةِ، آتِ مُحَمَّدًا الْوَسِيلَةَ وَالْفَضِيلَةَ، وَابْعَثْهُ مَقَامًا مَّحْمُودًا الَّذِي وَعَدْتَّهُ، إِنَّكَ لَا تُخْلِفُ الْمِيعَادَ",
            transliteration = "Allaahumma Rabba haadhihi-d-da'watit-tammah, was-salaatil-qaa'imah, aati Muhammadanil-waseelata wal-fadheelah, wab'ath-hu maqaaman mahmoodanil-ladhee wa'adtah, innaka laa tukhliful-mee'aad",
            translation = "O Allah! Lord of this perfect call and the established prayer, grant Muhammad the Wasilah (highest station in Paradise) and superiority, and resurrect him to the praised station which You have promised him. Truly You do not break Your promise."
        ),
        DuaItem(
            title = "Du'a-e-Qunoot (Witr Prayer)",
            occasion = "Recited in the third rakat of Witr prayer before bowing",
            arabic = "اَللّٰهُمَّ إِنَّا نَسْتَعِينُكَ وَنَسْتَغْفِرُكَ وَنُؤْمِنُ بِكَ وَنَتَوَكَّلُ عَلَيْكَ وَنُثْنِي عَلَيْكَ الْخَيْرَ، وَنَشْكُرُكَ وَلَا نَكْفُرُكَ، وَنَخْلَعُ وَنَتْرُكُ مَنْ يَّفْجُرُكَ. اَللّٰهُمَّ إِيَّاكَ نَعْبُدُ وَلَكَ نُصَلِّي وَنَسْجُدُ، وَإِلَيْكَ نَسْعَى وَنَحْفِدُ، وَنَرْجُو رَحْمَتَكَ وَنَخْشَى عَذَابَكَ، إِنَّ عَذَابَكَ بِالْكُفَّارِ مُلْحِقٌ",
            transliteration = "Allaahumma innaa nasta'eenuka wa nastaghfiruka wa nu'minu bika wa natawakkalu 'alayka wa nuthnee 'alaykal-khayr, wa nashkuruka wa laa nakfuruk, wa nakhla'u wa natruku man yafjuruk. Allaahumma iyyaaka na'budu wa laka nusallee wa nasjud, wa ilayka nas'aa wa nahfid, wa narjoo rahmataka wa nakhshaa 'adhaabak, inna 'adhaabaka bil-kuffaari mulhiq",
            translation = "O Allah! We seek Your assistance, and seek Your forgiveness, and believe in You, and put our trust in You, and praise You in the best manner, and thank You and are not ungrateful to You, and we renounce and leave whoever disobeys You. O Allah! You alone do we worship and to You do we pray and prostrate, and towards You do we strive and serve, and we hope for Your mercy and fear Your punishment. Truly Your punishment will overtake the disbelievers."
        ),
        DuaItem(
            title = "Ayat-ul-Kursi (The Throne Verse)",
            occasion = "Recited after every Farz prayer & before sleeping",
            arabic = "اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ ۚ لَا تَأْخُذُهُ سِنَةٌ وَلَا نَوْمٌ ۚ لَهُ مَا فِي السَّمَاوَاتِ وَمَا فِي الْأَرْضِ ۗ مَنْ ذَا الَّذِي يَشْفَعُ عِنْدَهُ إِلَّا بِإِذْنِهِ ۚ يَعْلَمُ مَا بَيْنَ أَيْدِيهِمْ وَمَا خَلْفَهُمْ ۖ وَلَا يُحِيطُونَ بِشَيْءٍ مِنْ عِلْمِهِ إِلَّا بِمَا شَاءَ ۚ وَسِعَ كُرْسِيُّهُ السَّمَاوَاتِ وَالْأَرْضَ ۖ وَلَا يَئُودُهُ حِفْظُهُمَا ۚ وَهُوَ الْعَلِيُّ الْعَظِيمُ",
            transliteration = "Allahu la ilaha illa Huwa, Al-Hayyul-Qayyum. La ta'khudhuhu sinatuw-wa la nawm. Lahu ma fis-samawati wa ma fil-ard. Man dhal-ladhi yashfa'u 'indahu illa bi-idhnih? Ya'lamu ma bayna aydihim wa ma khalfahum, wa la yuhituna bi-shay'im-min 'ilmihi illa bima sha'a. Wasi'a kursiyyuhus-samawati wal-arda, wa la ya'uduhu hifzuhuma, wa Huwal-'Aliyyul-'Adheem",
            translation = "Allah! There is no deity except Him, the Ever-Living, the Sustainer of all existence. Neither drowsiness overtakes Him nor sleep. To Him belongs whatever is in the heavens and whatever is on the earth. Who is it that can intercede with Him except by His permission? He knows what is before them and what will be after them, and they encompass not a thing of His knowledge except for what He wills. His Kursi extends over the heavens and the earth, and their preservation tires Him not. And He is the Most High, the Most Great."
        ),
        DuaItem(
            title = "Tasbeeh-e-Fatimah (After Salah)",
            occasion = "Recommended after each of the 5 obligatory prayers",
            arabic = "سُبْحَانَ اللهِ (٣٣) ، اَلْحَمْدُ لِلّٰهِ (٣٣) ، اَللهُ أَكْبَرُ (٣٤)",
            transliteration = "SubhanAllah (33 times), Alhamdulillah (33 times), Allahu Akbar (34 times)",
            translation = "Glory be to Allah (33x), Praise be to Allah (33x), Allah is the Greatest (34x)."
        )
    )

    val PRAYER_RAKATS = listOf(
        PrayerRakat("Fajr", 2, 2, 0, 0, 0, 4),
        PrayerRakat("Dhuhr", 4, 4, 2, 2, 0, 12),
        PrayerRakat("Asr", 4, 4, 0, 0, 0, 8),
        PrayerRakat("Maghrib", 0, 3, 2, 2, 0, 7),
        PrayerRakat("Isha", 4, 4, 2, 2, 3, 17),
        PrayerRakat("Jumu'ah", 4, 2, 4, 2, 0, 14)
    )

    val NAMAZ_STEPS = listOf(
        NamazStep(
            stepNumber = 1,
            stepName = "Niyyah (Intention)",
            arabicRecitation = "نَوَيْتُ أَنْ أُصَلِّيَ لِلّٰهِ تَعَالَى رَكَعَاتِ صَلَاةِ...",
            transliteration = "Nawaytu an usalliya lillaahi ta'aalaa...",
            explanation = "Make the firm intention in your heart for the specific prayer (e.g. 2 Rakats Farz of Fajr) facing towards the Qibla."
        ),
        NamazStep(
            stepNumber = 2,
            stepName = "Takbeer-e-Tahreema",
            arabicRecitation = "اللهُ أَكْبَرُ",
            transliteration = "Allahu Akbar",
            explanation = "Raise hands up to earlobes (men) or shoulders (women), saying Allahu Akbar, then fold hands below the navel (Hanafi) or chest."
        ),
        NamazStep(
            stepNumber = 3,
            stepName = "Sanaa (Opening Supplication)",
            arabicRecitation = "سُبْحَانَكَ اللّٰهُمَّ وَبِحَمْدِكَ وَتَبَارَكَ اسْمُكَ وَتَعَالَى جَدُّكَ وَلَا إِلٰهَ غَيْرُكَ",
            transliteration = "Subhaanak-Allaahumma wa bihamdika wa tabaarak-asmuka wa ta'aalaa jadduka wa laaa ilaaha ghayruk",
            explanation = "Recite Sanaa quietly, followed by Ta'awwudh (A'oodhu billah...) and Tasmiyah (Bismillah...)."
        ),
        NamazStep(
            stepNumber = 4,
            stepName = "Qiyam & Surah Fatiha",
            arabicRecitation = "الْحَمْدُ لِلّٰهِ رَبِّ الْعَالَمِينَ ...",
            transliteration = "Alhamdu lillaahi Rabbil 'aalameen...",
            explanation = "Recite Surah Al-Fatiha completely, followed by 'Ameen', and then recite another Surah or at least 3 short verses."
        ),
        NamazStep(
            stepNumber = 5,
            stepName = "Ruku (Bowing)",
            arabicRecitation = "سُبْحَانَ رَبِّيَ الْعَظِيمِ",
            transliteration = "Subhaana Rabbiyal 'Azeem (3 times)",
            explanation = "Bow down holding knees with fingers spread, back kept straight and head aligned. Say the Tasbeeh at least 3 times."
        ),
        NamazStep(
            stepNumber = 6,
            stepName = "Qawmah (Standing from Bowing)",
            arabicRecitation = "سَمِعَ اللهُ لِمَنْ حَمِدَهُ ، رَبَّنَا لَكَ الْحَمْدُ",
            transliteration = "Sami' Allaahu liman hamidah, Rabbanaa lakal hamd",
            explanation = "Stand straight up from Ruku, saying Sami' Allahu liman hamidah, followed by Rabbana lakal hamd while standing."
        ),
        NamazStep(
            stepNumber = 7,
            stepName = "Sajdah (Prostration)",
            arabicRecitation = "سُبْحَانَ رَبِّيَ الْأَعْلَى",
            transliteration = "Subhaana Rabbiyal A'laa (3 times)",
            explanation = "Prostrate placing knees first, then hands, nose, and forehead on the ground. Recite the Tasbeeh 3 times. Perform two Sajdahs with a brief sitting pause (Jalsah)."
        ),
        NamazStep(
            stepNumber = 8,
            stepName = "Tashahhud (Attahiyyat)",
            arabicRecitation = "التَّحِيَّاتُ لِلّٰهِ وَالصَّلَوَاتُ وَالطَّيِّبَاتُ، اَلسَّلَامُ عَلَيْكَ أَيُّهَا النَّبِيُّ وَرَحْمَةُ اللهِ وَبَرَكَاتُهُ، اَلسَّلَامُ عَلَيْنَا وَعَلَى عِبَادِ اللهِ الصَّالِحِينَ، أَشْهَدُ أَنْ لَا إِلٰهَ إِلَّا اللهُ وَأَشْهَدُ أَنَّ مُحَمَّدًا عَبْدُهُ وَرَسُولُهُ",
            transliteration = "Attahiyyaatu lillaahi was-salawaatu wat-tayyibaat. As-salaamu 'alayka ayyuhan-Nabiyyu wa rahmatullaahi wa barakaatuh. As-salaamu 'alaynaa wa 'alaa 'ibaadillaahis-saaliheen. Ash-hadu allaa ilaaha illallaahu wa ash-hadu anna Muhammadan 'abduhoo wa rasooluh",
            explanation = "Sit peacefully in the Qa'dah position after 2nd or final rakat and recite Tashahhud, followed by Durood-e-Ibrahimi in the final sitting."
        ),
        NamazStep(
            stepNumber = 9,
            stepName = "Salam (Conclusion)",
            arabicRecitation = "اَلسَّلَامُ عَلَيْكُمْ وَرَحْمَةُ اللهِ",
            transliteration = "As-salaamu 'alaykum wa rahmatullah",
            explanation = "Turn head to the right shoulder reciting the Salam, then turn to the left shoulder reciting the Salam."
        )
    )

    val TASBIH_PRESETS = listOf(
        TasbihPreset("subhanallah", "SubhanAllah", "سُبْحَانَ اللهِ", 33, "Glory be to Allah"),
        TasbihPreset("alhamdulillah", "Alhamdulillah", "اَلْحَمْدُ لِلّٰهِ", 33, "All praise is due to Allah"),
        TasbihPreset("allahuakbar", "Allahu Akbar", "اَللهُ أَكْبَرُ", 34, "Allah is the Greatest"),
        TasbihPreset("astaghfirullah", "Astaghfirullah", "أَسْتَغْفِرُ اللهَ", 100, "I seek forgiveness from Allah"),
        TasbihPreset("durood", "Durood Shareef", "صَلَّى اللّٰهُ عَلَيْهِ وَسَلَّمَ", 100, "Peace & Blessings upon the Prophet"),
        TasbihPreset("kalima", "Kalima Tayyibah", "لَا إِلٰهَ إِلَّا اللهُ", 100, "There is no God but Allah"),
        TasbihPreset("la_hawla", "La Hawla", "لَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللهِ", 33, "No power except with Allah")
    )
}
