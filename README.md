# Tabu — Kelime Anlatma Oyunu

Modern, minimalist, çevrimdışı (offline) ve tamamen reklamsız Android Tabu / Kelime Anlatma parti oyunu.

---

## 🎨 Tasarım ve Kullanıcı Deneyimi İlkeleri

- **Stil**: Flat Design ve Minimalizm. Parlak renkler, neon efektler veya dikkat dağıtıcı degrade (gradient) içermez.
- **Renk Paleti**: Gözü yormayan mat karanlık tema:
  - Zeminler: `#16181D`, `#1F2229`, `#292D36`
  - Metinler: Açık gri `#E4E6EC` ve sakin `#9399A8`
  - Vurgular & Butonlar: Yumuşak köşeli, mat renkler (Doğru `#7DB596`, Pas `#D1A86B`, Tabu `#C9757C`)
- **Sade Menü & Düzen**: Ana ekranda kalabalık listeler yerine özet satırları (`MenuRow`) yer alır. Düzenlemeler sade açılır pencereler (Dialog) üzerinden yapılır.
- **Sıfır Titreşim & Sıfır İzin**: Titreşim izni ve donanımı kullanılmaz. Sıfır internet izni ile %100 gizlilik ve çevrimdışı çalışma garantisi.
- **Minimal Ses**: İsteğe bağlı, hafif sistem bildirim tonları.

---

## 🎮 Oyun Akışı ve Özellikler

1. **Lobi / Hazırlık (Sade Ana Ekran)**:
   - **Takım Yönetimi**: "A Takımı" & "B Takımı" (Popup üzerinden isim düzenleme, takım ekleme/çıkarma — 2 ila 4 takım).
   - **Tur Süresi**: 45, 60, 90 veya 120 saniye seçenekleri.
   - **Hedef Puan**: 25, 50, 75, 100 puan veya limitsiz serbest oyun.
   - **Pas Hakkı**: 1, 2, 3, 5 pas veya limitsiz pas hakkı.
   - **Kategori Seçimi**: Tümü, Genel, Yiyecek & İçecek, Eşya & Teknoloji, Popüler Kültür & Dizi, Doğa & Mekan, Meslek & Kavram, Spor & Hobi.

2. **Tur Hazırlık Ekranı**:
   - Sıradaki takımı net olarak gösterir. Telefonu anlatıcıya devretmek için sakin bir geçiş alanı sunar.

3. **Oyun Ekranı**:
   - Sakin dijital sayaç (Son 10 saniyede hafif kehribar tonuna bürünür).
   - Sıradaki takım adı ve anlık tur puanı göstergesi.
   - Ortada mat kart üzerinde büyük **Hedef Kelime** ve altında 5 adet **Yasaklı Kelime**.
   - 3 Net Aksiyon Butonu:
     - 🟢 **Doğru (+1)**
     - 🟡 **Pas** (Hakkı bitince yumuşak şekilde pasifleşir)
     - 🔴 **Tabu (-1)**
   - Duraklatma (Pause) ve devam etme desteği.

4. **Tur Sonu & Skor Ekranı**:
   - Turda oynanan tüm kelimelerin net özeti.
   - **İtiraz & Düzeltme Özelliği**: Herhangi bir kelimeye dokunarak durumu (Doğru / Tabu / Pas) anında değiştirilebilir; puan canlı olarak yeniden hesaplanır.
   - Takımların genel sıralama ve puan tablosu.
   - Hedef puana ulaşıldığında şampiyon kutlaması ve yeni oyun seçeneği.

5. **Akıllı Deste Hafızası**:
   - 300'den fazla özenle hazırlanmış, kaliteli Türkçe kelime kartı.
   - Oynanan kelimelerin aynı oyun oturumunda tekrar gelmesini engelleyen deste hafızası.

---

## 🛠️ Teknik Özellikler

- **Platform**: Android Native
- **Dil**: Kotlin 2.0
- **UI Toolkit**: Jetpack Compose + Material 3
- **Minimum SDK**: 24 (Android 7.0+)
- **Target SDK**: 34 (Android 14+)
- **İzinler**: Sıfır izin (İnternet yok, titreşim yok, kamera/konum yok)
- **Depolama**: SharedPreferences (Ayarlar ve takım isimleri cihazda güvenle saklanır)

---

## 📦 Kurulum ve Çalıştırma

Hazır imzalı APK dosyası proje ana dizinindedir:
- `tabu-v1.0.0.apk`

Telefona doğrudan yüklemek için (USB Debugging açıkken):
```bash
adb install tabu-v1.0.0.apk
```

Projeyi sıfırdan derlemek için:
```bash
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk
./gradlew assembleRelease
```
