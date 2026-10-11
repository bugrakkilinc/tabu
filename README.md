# Tabu — Modern & Minimalist Kelime Anlatma Oyunu

<p align="center">
  <img src="app/src/main/res/drawable/ic_launcher.xml" width="96" height="96" alt="Tabu Logo" />
</p>

<p align="center">
  <b>Android için tamamen ücretsiz, reklamsız, çevrimdışı (%100 offline) çalışan parti oyunu.</b>
</p>

<p align="center">
  <a href="https://github.com/bugrakkilinc/tabu/releases/latest">
    <img src="https://img.shields.io/github/v/release/bugrakkilinc/tabu?color=8E9BD6&label=Son%20S%C3%BCr%C3%BCm&logo=android" alt="Son Sürüm" />
  </a>
  <a href="LICENSE">
    <img src="https://img.shields.io/badge/Lisans-MIT-7DB596.svg" alt="Lisans" />
  </a>
  <img src="https://img.shields.io/badge/Platform-Android-1F2229.svg" alt="Android" />
  <img src="https://img.shields.io/badge/UI-Jetpack%20Compose-8E9BD6.svg" alt="Jetpack Compose" />
  <img src="https://img.shields.io/badge/Reklam-Yok-7DB596.svg" alt="Reklamsız" />
</p>

<p align="center">
  <a href="https://github.com/bugrakkilinc/tabu/releases/latest">
    <img src="https://img.shields.io/badge/%E2%AC%87%EF%B8%8F_APK_%C4%B0ndir-Resmi_S%C3%BCr%C3%BCm-8E9BD6?style=for-the-badge" alt="APK İndir" />
  </a>
</p>

---

## ✨ Öne Çıkan Özellikler

- **Göz Yormayan Mat Karanlık Tema**: Parlak neonlar, degradeler ve dikkat dağıtıcı animasyonlar yerine dinlendirici mat antrasit zeminler (`#16181D`, `#1F2229`, `#292D36`).
- **Sade ve Hızlı Lobi**: Ana ekranda kalabalık listeler yerine özet satırları (`MenuRow`). Takımlar, süreler ve puanlar minimalist açılır pencereler (Dialog) üzerinden kolayca ayarlanır.
- **Sıfır Titreşim, Sıfır Ses, Sıfır İzin**: Titreşim donanımı veya sesi zorunlu kılmaz; internet izni dahil hiçbir tehlikeli izin istemez. %100 gizlilik ve sıfır pil tüketimi.
- **Zengin Türkçe Kelime Havuzu**: 300'den fazla özenle seçilmiş, güncel ve kaliteli Türkçe kelime kartı.
- **Akıllı Deste Hafızası**: Oynanan kelimeler aynı oyun oturumunda asla tekrar etmez.
- **Esnek Oyun Ayarları**:
  - **Tur Süresi**: 15 (Yıldırım), 20, 30 (Hızlı), 45, 60 (Standart), 90 veya 120 saniye.
  - **Hedef Puan**: 25, 50, 75, 100 puan veya limitsiz serbest oyun.
  - **Pas Hakkı**: 1, 2, 3, 5 veya limitsiz pas.
  - **Tabu Cezası**: Ceza Yok (0 Puan), -1 Puan (Klasik), -2 Puan (Zor) veya -3 Puan (Ağır Ceza).
  - **Kategoriler**: Tümü, Genel, Yiyecek & İçecek, Eşya & Teknoloji, Popüler Kültür & Dizi, Doğa & Mekan, Meslek & Kavram, Spor & Hobi.

---

## 📖 Nasıl Oynanır?

1. **Takımları Kurun**:
   - Lobi ekranında **Takımlar** satırına dokunarak takımlarınızı oluşturun ve isimlendirin (2 ila 4 takım).
2. **Ayarları Belirleyin**:
   - Tur süresini, hedef puanı, pas hakkını ve tabu ceza miktarını tercihinize göre ayarlayın.
3. **Turu Başlatın**:
   - Sırası gelen takımın anlatıcısı telefonu eline alır ve **Turu Başlat** butonuna basar.
4. **Anlatım ve Kurallar**:
   - **Hedef Kelime**: Kartın en üstünde büyük olarak yazan kelimedir. Takım arkadaşlarınıza bu kelimeyi anlatmaya çalışın.
   - **Yasaklı Kelimeler**: Kartın altında listelenen 5 kelimeyi ve bunların köklerini/türevlerini kullanmak **YASAKTIR**.
   - **Hareket / Jest**: Vücut dili veya el-kol işaretleriyle kelimeyi ima etmek yasaktır.
   - **Kafiye / Baş Harf**: "Bununla kafiyeli", "Şu harfle başlıyor" gibi ipuçları yasaktır.
5. **Aksiyon Butonları**:
   - 🟢 **Doğru (+1)**: Takımınız kelimeyi doğru bildiğinde basın.
   - 🟡 **Pas**: Anlatmakta zorlandığınız kelimeyi pas geçmek için basın (Pas hakkınız varsa).
   - 🔴 **Tabu**: Yasaklı kelimelerden birini söylediğinizde rakip takımın uyarısıyla basın (Ayarlanan ceza puanı düşer).
6. **Tur Sonu ve İtiraz Düzeltme**:
   - Süre bittiğinde o turda oynanan kelimelerin dökümü gösterilir. Tartışmalı veya yanlış basılan kelimelerin üzerine dokunarak durumunu (Doğru / Tabu / Pas) anında düzeltebilirsiniz.

---

## 🛠️ Teknik Altyapı

- **Dil**: Kotlin 2.0
- **Arayüz**: Jetpack Compose + Material 3
- **Mimari**: MVVM (Model-View-ViewModel) + State Hoisting
- **Min SDK**: 24 (Android 7.0+)
- **Target SDK**: 34 (Android 14+)
- **İmzalama**: Resmi 2048-bit RSA Keystore (Full v1, v2, v3, v4 şemaları)
- **CI/CD**: GitHub Actions ile otomatik derleme ve Release dağıtımı

---

## 📦 Kurulum ve Derleme

### Doğrudan Yükleme (Hazır APK)
En güncel imzalı APK dosyasını [GitHub Releases](https://github.com/bugrakkilinc/tabu/releases/latest) sayfasından indirip telefonunuza yükleyebilirsiniz.

ADB üzerinden kurmak için:
```bash
adb install tabu-v1.0.0.apk
```

### Projeyi Kaynak Koddan Derleme
```bash
git clone git@github.com:bugrakkilinc/tabu.git
cd tabu
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk # veya kurulu JDK 17 dizini
./gradlew assembleRelease
```
Derlenen imzalı APK: `app/build/outputs/apk/release/app-release.apk`

---

## 📄 Lisans

Bu proje [MIT Lisansı](LICENSE) altında açık kaynak olarak sunulmaktadır.
