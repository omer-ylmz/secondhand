# Secondhand Project

Bu proje, ikinci el ürünlerin alım satımını kolaylaştırmayı amaçlayan, modüler bir Java Spring Boot uygulamasıdır. Proje, ölçeklenebilir ve maintainable (bakımı kolay) bir yapı sunmak amacıyla çok modüllü (multi-module) Gradle yapısı üzerine inşa edilmiştir.

## 🗂 Proje Yapısı ve Mimarisi

Proje, temel iş mantığını ve konfigürasyonları ayıran modüler bir mimariye sahiptir:

*   **secondhand-configuration**: Uygulamanın giriş noktasıdır (Main Application). Tüm Spring Boot konfigürasyonlarını ve uygulamanın başlatılmasından sorumlu `SecondhandApplication` sınıfını barındırır.
*   **user**: Kullanıcı yönetimi işlemlerini (Kayıt, Güncelleme, Listeleme, Silme/Pasife Alma) içeren modüldür.
    *   **Controller**: REST API uç noktalarını açar (`UserController`).
    *   **Service**: İş mantığını barındırır (`UserService`).
    *   **Repository**: Veritabanı erişim katmanıdır (JPA).
    *   **Model**: Veritabanı varlıklarını tanımlar (`User`, `BaseEntity`).
    *   **DTO, Mapper, Exception**: Veri transfer nesneleri ve hata yönetimi bileşenleri.

## 🛠 Kullanılan Teknolojiler

Proje geliştirilirken aşağıdaki modern teknolojiler ve kütüphaneler kullanılmıştır:

*   **Dil:** Java 17 (ve Kotlin desteği)
*   **Framework:** Spring Boot 3.2.1
*   **Build Aracı:** Gradle (Kotlin DSL ve Groovy DSL karışık kullanım)
*   **Veritabanı:**
    *   **Geliştirme/Test:** H2 Database (In-Memory)
    *   **Prodüksiyon:** MySQL
*   **ORM:** Spring Data JPA
*   **Araçlar ve Kütüphaneler:**
    *   **Lombok:** Boilerplate kodları (Getter, Setter, Constructor vb.) azaltmak için.
    *   **MapStruct:** Entity ve DTO dönüşümleri için.
    *   **Docker:** Uygulamanın konteynerize edilmesi için.

## 🚀 Kurulum ve Çalıştırma

Projeyi yerel ortamınızda çalıştırmak için aşağıdaki adımları takip edebilirsiniz.

### Gereksinimler
*   JDK 17 veya üzeri
*   Docker (Opsiyonel, veritabanı veya container build için)

### Terminal ile Çalıştırma

Proje kök dizininde aşağıdaki komutu kullanarak uygulamayı başlatabilirsiniz:

**Windows:**
```powershell
.\gradlew bootRun
```

**Linux/Mac:**
```bash
./gradlew bootRun
```

### Docker ile Build Alma ve Çalıştırma

Projede çok aşamalı (multi-stage) bir `Dockerfile` bulunmaktadır. Bu sayede uygulama Docker üzerinde derlenip minimal bir imaj olarak çalıştırılabilir.

1.  **Docker İmajını Oluşturma:**
    ```bash
    docker build -t secondhand-app .
    ```

2.  **Konteyneri Çalıştırma:**
    ```bash
    docker run -p 9091:8080 secondhand-app
    ```

3.  **Docker Compose:**
    Projede bir `docker-compose.yaml` dosyası mevcuttur (şu an için boş olabilir), veritabanı gibi bağımlılıkları ayağa kaldırmak için yapılandırılabilir.

## 📡 API Kullanımı (User Module)

Uygulama çalıştığında `http://localhost:9091` üzerinden erişilebilir. `user` modülü aşağıdaki endpointleri sunar (`/v1/user` altında):

| Metot | Endpoint | Açıklama |
| :--- | :--- | :--- |
| `GET` | `/v1/user` | Tüm kullanıcıları listeler. |
| `GET` | `/v1/user/{mail}` | Mail adresine göre kullanıcı detaylarını getirir. |
| `POST` | `/v1/user` | Yeni bir kullanıcı oluşturur. Body: `CreateUserRequest` |
| `PUT` | `/v1/user/{mail}` | Kullanıcı bilgilerini günceller. Body: `UpdateUserRequest` |
| `PATCH` | `/v1/user/{id}` | Kullanıcıyı pasif hale getirir (Soft delete). |
| `PATCH` | `/v1/user/{id}/active` | Pasif kullanıcıyı tekrar aktif hale getirir. |
| `DELETE` | `/v1/user/{id}` | Kullanıcıyı siler (veya konfigürasyona göre pasife alır). |

## 👨‍💻 Geliştirici Notları

*   **MapStruct & Lombok:** Projede MapStruct ve Lombok birlikte kullanıldığı için build konfigürasyonlarında `annotationProcessor` sıralamalarına dikkat edilmiştir.
*   **Paket Yapısı:** `com.omer.secondhand` altında modüler bir paketleme yapısı izlenmiştir.
*   **Test:** `./gradlew test` komutu ile birim testleri koşturabilirsiniz.

---
*Bu dokümantasyon projenin mevcut durumuna göre oluşturulmuştur.*

## 🐳 Docker Deployment Guide (AWS Support)

Bu proje, Docker kullanılarak konteynerize edilmiştir. Aşağıdaki adımları takip ederek projeyi sunucunuzda (AWS EC2 vb.) kolayca yayına alabilirsiniz.

### 1. İmajın Oluşturulması (Build)
Projenin ana dizininde Docker imajını oluşturun:

```bash
docker build -t secondhand-app .
```

### 2. İmajın Sunucuya Transferi (Offline Yöntem)
Docker Hub kullanmadan, oluşturulan imajı doğrudan sunucuya göndermek için:

**Adım A: İmajı dosya haline getirin (.tar)**
```bash
docker save -o secondhand-image.tar secondhand-app
```

**Adım B: Dosyayı sunucuya kopyalayın (SCP)**
```bash
scp -i "anahtar-dosyaniz.pem" secondhand-image.tar ec2-user@<SUNUCU_IP_ADRESI>:~/
```

### 3. Sunucuda Kurulum ve Çalıştırma
Sunucuya SSH ile bağlandıktan sonra aşağıdaki komutları uygulayın:

**Adım A: İmajı Docker'a yükleyin**
```bash
sudo docker load -i secondhand-image.tar
```

**Adım B: Uygulamayı başlatın**
Aşağıdaki komut uygulamayı 80 portunda başlatır ve sunucu yeniden başlasa bile otomatik olarak çalışmasını sağlar (`--restart always`).

```bash
sudo docker run -d \
  --restart always \
  -p 80:8080 \
  --name secondhand-server \
  secondhand-app
```

### 4. Yönetim Komutları

**Logları Canlı İzlemek İçin:**
```bash
sudo docker logs -f secondhand-server
```

**Uygulamayı Durdurmak İçin:**
```bash
sudo docker stop secondhand-server
```

**Uygulamayı Yeniden Başlatmak İçin:**
```bash
sudo docker restart secondhand-server
```

### 🌍 Canlı Sunucu
Şu anki aktif sunucu erişimi:
[http://51.20.131.151/v1/user](http://51.20.131.151/v1/user)

