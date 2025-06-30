package com.vural.fitresttimer

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.IBinder
import androidx.compose.ui.graphics.Color
import androidx.core.app.NotificationCompat

class TimerService : Service() {
    val fitnessTips = listOf(
        "Kas geliştirmek için uykunuza dikkat edin!",
        "Ağırlık kaldırırken doğru form, ağırlıktan daha önemlidir.",
        "Yeterli su içmek performansınızı artırır.",
        "Her antrenmandan önce iyi bir ısınma yapın.",
        "Dinlenme, kasların büyümesi için şarttır.",
        "Dengeli beslenme, fitness başarısının anahtarıdır.",
        "Esneme, sakatlanma riskini azaltır.",
        "Protein alımını ihmal etmeyin.",
        "Antrenman yoğunluğunu yavaş yavaş artırın.",
        "Motivasyonunuzu yüksek tutmak için hedef koyun.",
        "Sadece spor yaparak değil, yeterli uyuyarak da yağ yakarsınız.",
        "Sabah aç karnına yapılan kardiyo yağ yakımını hızlandırabilir.",
        "Her gün farklı kas grubuna odaklanmak gelişimi destekler.",
        "Kas geliştirmek için her setin sonuna yakın tükenişe ulaşın.",
        "Sıvı kaybı kas kramplarının en yaygın nedenidir.",
        "Kaslarınızı büyütmek istiyorsanız, ilerlemeyi (progressive overload) takip edin.",
        "Güçlü bir core bölgesi, sırt ağrılarını azaltır.",
        "Dizler ayak parmaklarını geçmemeli kuralı bilimsel olarak zorunlu değildir.",
        "Squat derinliği kalçanın hareket açıklığına göre ayarlanmalı.",
        "Dinlenme günleri, kas büyümesi için aktif günler kadar önemlidir.",
        "Çok fazla kardiyo kas kaybına yol açabilir.",
        "Antrenman sonrası hemen protein almak zorunlu değildir.",
        "Yoğunluk arttıkça toparlanma süresi de artmalı.",
        "Ağır kaldırmadan önce ısınmak, sakatlanmayı önler.",
        "Vücudunuzu dinleyin, ağrı varsa zorlamayın.",
        "Yoga ve pilates, dayanıklılığı ve esnekliği artırır.",
        "Çay ve kahve, antrenman öncesi enerji seviyesini artırabilir.",
        "Aynı kas grubunu haftada 2-3 kez çalıştırmak genellikle daha etkilidir.",
        "Egzersiz sırasında ağızdan nefes almak oksijen alımını artırabilir.",
        "Sakatlanmaların %80’i yanlış teknikten kaynaklanır.",
        "Kreatin, kas gelişimi için en çok araştırılmış ve güvenli takviyedir.",
        "Kilo kaybı için en önemli faktör kalori açığıdır.",
        "Sık sık tartılmak motivasyonu düşürebilir.",
        "Ağırlık çalışırken yavaş ve kontrollü hareketler sakatlanma riskini azaltır.",
        "Soğuk duş, kasların toparlanmasına yardımcı olabilir.",
        "Karın kasları haftada 3’ten fazla çalıştırılmamalı.",
        "Kahvaltıyı atlamak metabolizmayı yavaşlatmaz.",
        "Uzun süreli açlık kas kaybına neden olabilir.",
        "Sürekli aynı antrenmanı yapmak ilerlemeyi durdurur.",
        "D vitamini eksikliği performansı düşürebilir.",
        "Sırt kaslarını güçlendirmek duruşu düzeltir.",
        "Kilo almak için kalori fazlası gereklidir.",
        "Antrenman sonrası esneme, kas sertliğini azaltabilir.",
        "Gün içinde kısa yürüyüşler yapmak kan dolaşımını artırır.",
        "Ağır kaldırmak kemik yoğunluğunu artırır.",
        "Lifli besinler sindirim sağlığını destekler.",
        "Her hareketi aynada kontrol etmek vücut farkındalığını artırır.",
        "Fitness yolculuğu uzun bir maratondur, sabırlı olun.",
        "Günde 10.000 adım hedefi, genel sağlık için uygundur.",
        "Her 30 dakikada bir kısa hareket molası verin.",
        "Kendi vücut ağırlığınızla yapılan egzersizler de çok etkilidir.",
        "Kardiyo egzersizleri kalp sağlığını korur.",
        "Haftada en az 150 dakika orta yoğunlukta egzersiz önerilir.",
        "İdeal protein miktarı kilo başına 1,6-2,2 gramdır.",
        "Yavaş yemek yemek daha az kalori almanızı sağlar.",
        "Kas kütlesi arttıkça bazal metabolizma da artar.",
        "Sıcak havada egzersiz sırasında su kaybı daha hızlı olur.",
        "Ağırlık çalışırken nefes kontrolü önemlidir.",
        "Direnç bandı egzersizleri sakatlık sonrası için idealdir.",
        "İnatçı yağ bölgeleri genetik olarak belirlenir.",
        "Antrenman sonrası magnezyum kas gevşemesine yardımcı olur.",
        "Sporda ilerleme için küçük hedefler koymak önemlidir.",
        "Protein sentezi antrenmandan sonra 24-48 saat sürer.",
        "Güçlü bacaklar koşu performansını artırır.",
        "Karbonhidratlar kas glikojenini yeniler.",
        "Kas ağrısı, iyi bir antrenman yaptığınız anlamına gelmez.",
        "Sırt egzersizleri duruşu düzeltir.",
        "Stretching hareketleri esnekliği artırır.",
        "İdeal uyku süresi yetişkinler için 7-9 saattir.",
        "Egzersiz sonrası çikolatalı süt kas onarımı için uygundur.",
        "Kilo vermek için tek bir doğru diyet yoktur.",
        "Mide kaslarını göstermek için düşük yağ oranı gereklidir.",
        "Her gün aynı saatte egzersiz yapmak alışkanlık oluşturur.",
        "Şınav, üst vücut kasları için mükemmel bir egzersizdir.",
        "Yoğurt ve kefir bağırsak sağlığını destekler.",
        "Her antrenmandan önce ısınmayı ihmal etmeyin.",
        "Daha fazla kas için ağırlık ve tekrarları yavaşça artırın.",
        "Hızlı kilo vermek genellikle geri alınır.",
        "Maksimum tekrar (1RM) testi dikkatli yapılmalıdır.",
        "Mental olarak hazırlık da fiziksel kadar önemlidir.",
        "Fazla kardiyo kas kaybına yol açabilir.",
        "Diz üstü mekik bele yük bindirmez.",
        "Mekik ile bel ağrısı arasında ilişki olabilir.",
        "Hedefinize uygun bir program seçin.",
        "Egzersiz sırasında sıvı kaybına dikkat edin.",
        "İleri yaşlarda egzersiz kemik sağlığını korur.",
        "Elma ve armut vücut tipi yağ dağılımında etkilidir.",
        "Düzenli spor depresyon riskini azaltır.",
        "Yağ yakımı için nabzınızı orta seviyede tutun.",
        "Fazla şeker insülin direncini artırabilir.",
        "Uzun süre aynı pozisyonda oturmak metabolizmayı yavaşlatır.",
        "Spor sonrası hızlı karbonhidrat kas glikojenini yeniler.",
        "Yüksek proteinli diyet böbrek hastalarına önerilmez.",
        "Bol sebze ve meyve antioksidan kaynağıdır.",
        "Kafein performansı %5-10 artırabilir.",
        "Kardiyo, ağırlık sonrası daha etkili olabilir.",
        "Karın kasları vücudun en hızlı adapte olan kaslarındandır.",
        "Bölgesel yağ yakımı diye bir şey yoktur.",
        "Çok fazla diyet yapmak metabolizmayı yavaşlatır.",
        "Sürekli tartılmak psikolojiyi olumsuz etkileyebilir.",
        "Süt ürünleri kemik sağlığı için önemlidir.",
        "Az su içmek metabolizmayı yavaşlatır.",
        "Uyku sırasında büyüme hormonu salınımı artar.",
        "Birden fazla antrenman çeşidini birleştirmek platoyu kırabilir.",
        "L-karnitin yağ yakımında etkisi sınırlıdır.",
        "Omega-3 yağ asitleri kas onarımına yardımcı olur.",
        "Çok tuz tüketmek ödem yapar.",
        "Düşük karbonhidrat diyeti bazı insanlarda enerji düşüklüğüne yol açabilir.",
        "Fazla yağ, eklemlere baskı yapar.",
        "Çok ağır kaldırmak sakatlanmaya neden olabilir.",
        "Soğuk havada kaslar daha geç ısınır.",
        "Yoğun tempolu yürüyüş kalp sağlığını artırır.",
        "Her antrenman öncesi hedef belirlemek motivasyonu artırır.",
        "Antrenman partneriyle çalışmak verimi artırır.",
        "HIIT antrenmanları zamandan tasarruf sağlar.",
        "Su tüketimi yaşa ve kiloya göre değişir.",
        "Çok sık tartılmak kilo verme sürecini olumsuz etkiler.",
        "Glutamin bağışıklık sistemini destekleyebilir.",
        "Kendinizi başkalarıyla kıyaslamak motivasyonu düşürür.",
        "Kilo almak için kas yapmaya odaklanın.",
        "Karın kası yapmak için düşük yağ oranı gerekir.",
        "Kahvaltı yapmadan da kas yapılabilir.",
        "Bacak antrenmanını atlamayın.",
        "Her gün spor yapmak aşırıya kaçmak olabilir.",
        "Meyve suyu yerine taze meyve tüketin.",
        "Kilo vermek için düşük karbonhidrat değil kalori açığı gerekir.",
        "Biceps kası, kolun en hızlı adapte olan kasıdır.",
        "Alkol, kas gelişimini yavaşlatabilir.",
        "Bitkisel proteinler de kas yapımında etkilidir.",
        "Kalsiyum eksikliği kas kramplarına neden olur.",
        "Basit şekerlerden uzak durmak yağlanmayı azaltır.",
        "Protein tozu zorunlu değildir.",
        "Küçük kas grupları daha hızlı toparlanır.",
        "Her antrenman sonrası soğuma yapın.",
        "Düşük karbonhidrat diyeti herkes için uygun değildir.",
        "Doğru nefes almak performansı artırır.",
        "Lifli gıdalar tokluk hissi verir.",
        "Kas kütlesi yaşla azalır, direnç antrenmanı önemlidir.",
        "Stres kortizol salgısını artırır.",
        "Zencefil kas ağrılarını azaltabilir.",
        "Sürekli aynı tempoda koşmak gelişmeyi yavaşlatır.",
        "Dinlenme gününde hafif yürüyüş yapabilirsiniz.",
        "Her gün yumurta yemek şart değildir.",
        "Fazla yağlı yemek sindirimi yavaşlatır.",
        "Sebze suyu içmek bağışıklığı güçlendirebilir.",
        "Aşırı kafein uykusuzluk yapar.",
        "Veganlar için B12 takviyesi gereklidir.",
        "Çok fazla kardiyo kas kaybına yol açabilir.",
        "Spor öncesi açlık, performansı düşürebilir.",
        "Magnezyum eksikliği kas kramplarına neden olabilir.",
        "Mide kasları diğer kaslardan daha zor belirginleşir.",
        "Beyaz ekmek yerine tam tahıllı ekmek tüketin.",
        "Yavaş sindirilen karbonhidratlar enerji sağlar.",
        "Aşırı su tüketimi de zararlı olabilir.",
        "İyi bir antrenman için öncelik uyku ve beslenme.",
        "Her sporcu kendine uygun programı bulmalı.",
        "Yarışmalarda karbonhidrat yüklemesi stratejiktir.",
        "Karın kası yapmak için mekik şart değildir.",
        "Günde 2 litre su içmek herkes için geçerli olmayabilir.",
        "Kilo vermek için sabırlı olun.",
        "Yağsız kas kütlesi metabolizmayı hızlandırır.",
        "Aç karnına ağırlık kaldırmak önerilmez.",
        "Güç antrenmanından sonra kas ağrısı normaldir.",
        "Açık havada egzersiz D vitamini sağlar.",
        "Fazla kahve kalp ritmini bozabilir.",
        "Brokoli antioksidan açısından zengindir.",
        "Her antrenman sonrası protein almak gerekmeyebilir.",
        "Karbonhidratlar beyin fonksiyonları için gereklidir.",
        "Çok su içmek böbreklere zarar verebilir.",
        "Ağırlık çalışırken müzik dinlemek motivasyonu artırır.",
        "Zeytinyağı kalp sağlığı için faydalıdır.",
        "Avokado sağlıklı yağ içerir.",
        "Soğuk havada koşmak kalori yakımını artırabilir.",
        "Sürekli motivasyon beklemeyin, disiplin önemlidir.",
        "Kas geliştirmek için protein tek başına yeterli değildir.",
        "Aynı anda hem kas yapıp hem yağ yakmak zordur.",
        "Kaliteli bir spor ayakkabı sakatlanma riskini azaltır.",
        "Yavaş kilo vermek daha sağlıklıdır.",
        "Günde 1 muz potasyum ihtiyacını karşılar.",
        "Her sporcu farklı hızda gelişir.",
        "Kilo almak için sağlıklı yağları artırabilirsiniz.",
        "Çok fazla şeker karaciğeri yağlandırabilir.",
        "Antrenman sonrası karbonhidrat kas onarımını hızlandırır.",
        "Fazla protein böbrekleri yorabilir.",
        "Her antrenmandan önce kısa bir yürüyüş yapın.",
        "Tatlı ihtiyacını meyveyle giderebilirsiniz.",
        "Egzersiz öncesi kafein, dayanıklılığı artırabilir.",
        "Güçlü kalçalar bel ağrısını azaltır.",
        "Kilo vermek için az yemek yerine dengeli beslenin.",
        "Düşük yağlı süt ürünleri de sağlıklıdır.",
        "Kas oranı fazla olanlar daha fazla kalori yakar.",
        "Her set arasında nefeslenmek toparlanmayı artırır.",
        "Kuru yemişler sağlıklı yağ kaynağıdır.",
        "Kilo kontrolü için porsiyon kontrolü önemlidir.",
        "Yüksek lifli diyet sindirimi kolaylaştırır.",
        "Her gün aynı kas grubunu çalıştırmak sakatlık riskini artırır.",
        "Ağırlık çalışırken vücut dengesini korumak önemlidir.",
        "Fazla karbonhidrat yağ depolanmasına neden olabilir.",
        "Aralıklı oruç bazı kişilerde enerji artışı sağlayabilir.",
        "Yağ yakımı için sabırlı ve disiplinli olun.",
        "Çok uzun süren antrenmanlar verimi azaltabilir.",
        "Koşu sırasında kolların hareketi önemlidir.",
        "Kilo vermek için sağlıklı atıştırmalıklar tercih edin.",
        "Her gün aynı saatte uyumak hormon dengesini korur.",
        "Su tüketimini gün içine yaymak önemlidir.",
        "Çok düşük kalorili diyetler kas kaybına neden olabilir.",
        "Her antrenman sonunda esneme yapın.",
        "Kas yapmak için sadece spor yetmez, beslenme de çok önemlidir.",
        "Antrenman partneriyle çalışmak motivasyonu artırır.",
        "Yüksek yoğunluklu antrenmanlar kısa sürede etkilidir.",
        "Sağlıklı bir yaşam için hareketli olun.",
        "Kas kütlesini artırmak için sabırlı olun.",
        "Vücut ağırlığı egzersizleri de kas yapar.",
        "Antrenman sonrası soğuk duş toparlanmayı hızlandırabilir.",
        "Aşırı spor yapmak bağışıklığı zayıflatabilir.",
        "Kas geliştirmek için yağdan korkmayın.",
        "Her sabah güne limonlu su ile başlayabilirsiniz.",
        "Fazla tuz tansiyonu yükseltebilir.",
        "Her sporcu farklı diyetlere farklı yanıt verir.",
        "Kaslarınızı büyütmek için yavaş çalışın.",
        "Stres, kas gelişimini olumsuz etkiler.",
        "Açık havada yapılan egzersiz ruh halini iyileştirir.",
        "Yoğun spor sonrası yeterli uyku şarttır.",
        "Kalsiyum kemik sağlığı için gereklidir.",
        "Günlük lif ihtiyacınızı sebzelerden alın.",
        "Kas geliştirmek için set aralarını kısa tutun.",
        "Her kas grubunu eşit çalıştırın.",
        "Çok tuz tüketimi ödem yapar.",
        "Kilo vermek için aç kalmak gerekmez.",
        "Makarna ve pilavı ölçülü tüketin.",
        "Kilo alımında genetik de etkilidir.",
        "Beyaz ekmek yerine tam buğday ekmeği tercih edin.",
        "Çok az su içmek halsizlik yapar.",
        "Spor sonrası tartılmak yanıltıcı olabilir.",
        "Protein tozu tek başına mucize değildir.",
        "Egzersiz sonrası hemen duş almak gerekmez.",
        "Kilo kaybı için kalori açığı oluşturun.",
        "Aşırı diyet yapmak metabolizmayı yavaşlatır.",
        "Kas gelişiminde yaş önemli değildir.",
        "Ağırlık kaldırmak kadınlarda kas şişkinliği yapmaz.",
        "Bacak kasları vücudun en büyük kas grubudur.",
        "Spor sonrası yemek yemek kas onarımını hızlandırır.",
        "Haftada 1 gün tamamen dinlenmek iyidir.",
        "Yeterli demir eksikliği yorgunluk yapar.",
        "Kas kaybını önlemek için protein tüketin.",
        "Kilo vermek için gün boyunca hareket edin.",
        "Zencefil, kas ağrılarını azaltabilir.",
        "Yağ yakımında interval antrenman etkilidir.",
    )
    private var remainingSeconds = 0
    private var currentMode: String = "bodybuilding"
    private var timer: java.util.Timer? = null
    val finishedChannelId = "finished_channel"

    private fun cancelCountdown() {
        timer?.cancel()
        remainingSeconds = 0
        // Bildirimi güncelle (veya yok et)
        stopForeground(true) // Bildirimi de tamamen kaldırır
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(1) // 1: timer notificationId
    }
    private fun showTimeUpNotification() {
        val finishedChannelId = "finished_channel"
        val notification: Notification = NotificationCompat.Builder(this, finishedChannelId)
            .setContentTitle("Süre doldu!")
            .setContentText("Antrenman Zamanı.")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .addAction(0, "30 sn", getPendingIntent("ACTION_TIMER_30"))
            .addAction(0, "45 sn", getPendingIntent("ACTION_TIMER_45"))
            .addAction(0, "60 sn", getPendingIntent("ACTION_TIMER_60"))
            .build()
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //notificationManager.cancel(1)
        notificationManager.notify(1, notification)

    }
    private fun getPendingIntent(action: String): PendingIntent {

        val intent = Intent(this, TimerService::class.java).apply { this.action = action }
        val requestCode = when(action) {
            "ACTION_TIMER_30" -> 0
            "ACTION_TIMER_45" -> 1
            else -> 0
        }
        return PendingIntent.getService(this, requestCode, intent, PendingIntent.FLAG_IMMUTABLE)
    }
    private fun updateNotification(seconds: Int, tip: String) {
        val channelId = "fitrest_basic_channel"

        val actions = if (currentMode == "powerlifting") {
            listOf(
                Pair("ACTION_TIMER_90", "90 sn"),
                Pair("ACTION_TIMER_120", "120 sn"),
                Pair("ACTION_TIMER_180", "180 sn")
            )
        } else {
            listOf(
                Pair("ACTION_TIMER_30", "30 sn"),
                Pair("ACTION_TIMER_45", "45 sn"),
                Pair("ACTION_TIMER_60", "60 sn")
            )
        }

        val builder = NotificationCompat.Builder(this, channelId)
            .setContentText("Hatırlatma: $tip")
            .setContentTitle("Kalan süre: $seconds sn")
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)

        actions.forEach { (action, label) ->
            builder.addAction(0, label, getPendingIntent(action))
        }
        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, builder.build())
    }
    private fun startCountdown(seconds: Int, ) {
        timer?.cancel()
        remainingSeconds = seconds

        val randomTip = fitnessTips.random()

        timer = java.util.Timer()
        timer?.scheduleAtFixedRate(object : java.util.TimerTask() {
            override fun run() {
                if (remainingSeconds <= 0) {
                    timer?.cancel()
                    showTimeUpNotification()
                } else {
                    updateNotification(remainingSeconds, randomTip)
                    remainingSeconds--
                }
            }
        }, 0, 1000)
    }


    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val incomingMode = intent?.getStringExtra("mode")
        if (incomingMode != null) {
            currentMode = incomingMode
        }

        val actions = if (currentMode == "powerlifting") {
            listOf(
                Pair("ACTION_TIMER_90", "90 sn"),
                Pair("ACTION_TIMER_120", "120 sn"),
                Pair("ACTION_TIMER_180", "180 sn")
            )
        } else {
            listOf(
                Pair("ACTION_TIMER_30", "30 sn"),
                Pair("ACTION_TIMER_45", "45 sn"),
                Pair("ACTION_TIMER_60", "60 sn")
            )
        }
        when (intent?.action) {
            "ACTION_TIMER_30" -> startCountdown(30)
            "ACTION_TIMER_45" -> startCountdown(45 )
            "ACTION_TIMER_60" -> startCountdown(60 )
            "ACTION_TIMER_90" -> startCountdown(90 )
            "ACTION_TIMER_120" -> startCountdown(120)
            "ACTION_TIMER_180" -> startCountdown(180)
            "ACTION_CANCEL" -> cancelCountdown()
        }
        val actionCancelIntent = Intent(this, TimerService::class.java).apply {
            action = "ACTION_CANCEL"
        }
        val pendingCancel = PendingIntent.getService(this, 3, actionCancelIntent, PendingIntent.FLAG_IMMUTABLE)
        val action30Intent = Intent(this, TimerService::class.java).apply {
            action = "ACTION_TIMER_30"
        }
        val action45Intent = Intent(this, TimerService::class.java).apply {
            action = "ACTION_TIMER_45"
        }
        val pending30 = PendingIntent.getService(this, 0, action30Intent, PendingIntent.FLAG_IMMUTABLE)
        val pending45 = PendingIntent.getService(this, 1, action45Intent, PendingIntent.FLAG_IMMUTABLE)
        val channelId = "fitrest_basic_channel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // 1. Sayaç bildirimi kanalı (sessiz, düşük öncelikli)
            val channel = NotificationChannel(
                channelId,
                "Basic Channel",
                NotificationManager.IMPORTANCE_LOW // Sessiz, sadece ekranda
            )
            // Burada sese gerek yok çünkü sessiz olmalı
            notificationManager.createNotificationChannel(channel)

            // 2. Bitiş bildirimi kanalı (yüksek öncelikli, sesli, titreşimli)
            val finishedChannel = NotificationChannel(
                finishedChannelId,
                "Süre Doldu Bildirimi",
                NotificationManager.IMPORTANCE_HIGH // Banner, ses, titreşim
            )
            finishedChannel.enableVibration(true)
            finishedChannel.vibrationPattern = longArrayOf(0, 400, 200, 400)
            // İstersen ses ekle:
            val soundUri = Uri.parse("android.resource://" + packageName + "/" + R.raw.rest_end)
            val audioAttributes = android.media.AudioAttributes.Builder()
                .setContentType(android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(android.media.AudioAttributes.USAGE_NOTIFICATION)
                .build()
            finishedChannel.setSound(soundUri, audioAttributes)
            notificationManager.createNotificationChannel(finishedChannel)
        }

        val notificationId = 1

        val builder = NotificationCompat.Builder(this, channelId)
            .setContentTitle("${currentMode.replaceFirstChar { it.uppercaseChar() }} Modu")
            .setStyle(NotificationCompat.BigTextStyle().bigText("Dinlenme sürenizi seçiniz"))

            .setSmallIcon(R.drawable.ic_notification_icon)

        actions.forEachIndexed { index, (action, label) ->
            builder.addAction(0, label, getPendingIntent(action))
        }
        val notification = builder.build()
        startForeground(notificationId, notification)



        return START_NOT_STICKY
    }
}