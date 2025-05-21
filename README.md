# 👗 SnapClothes – AR-Based Virtual Try-On Fashion App

**SnapClothes** is a next-gen AR fashion app that lets users virtually try on 3D-designed clothing in real time using Snapchat’s Camera Kit. Built with a modern Android tech stack and enhanced with AI assistance and secure authentication, SnapClothes reimagines the online fashion experience.

---

## 🌟 Features

- 📸 **Virtual Try-On with AR**  
  Use Snapchat Camera Kit and custom-designed lenses to visualize outfits in real-time.

- 🧵 **Realistic 3D Clothing**  
  Garments are designed in **Marvelous Designer** and textured using **Substance 3D Painter**, ensuring a lifelike try-on experience.

- 🛍️ **Product-Linked Lenses**  
  Each AR lens is tied to a specific product with details, so users can instantly explore or buy.

- 🌀 **Lens Carousel Interface**  
  A sleek swipeable carousel (like Snapchat) lets users browse and select outfits with ease.

- 🤖 **AI Fashion Assistant**  
  Integrated **Gemini-powered chatbot** offers fashion tips, size suggestions, and styling advice based on user preferences.

- 🔐 **Secure User Authentication**  
  User login and onboarding are handled through **Firebase Authentication**, enabling personalized wardrobe access.

---

## 🛠️ Tech Stack

| Layer              | Technology                                      |
|-------------------|--------------------------------------------------|
| **Frontend**       | Kotlin, Jetpack Compose                         |
| **AR Lenses**      | Lens Studio (Snapchat Camera Kit)              |
| **3D Design**      | Marvelous Designer                              |
| **Texturing**      | Adobe Substance 3D Painter                      |
| **Authentication** | Firebase Auth                                   |
| **AI Assistant**   | Gemini API (via Bard/Gemini Pro)                |
| **UI Styling**     | Jetpack Compose, Material 3, animations         |

---

## 🔗 Backend Service – Flask + MongoDB (Hosted on Fly.io)

The SnapClothes backend is a lightweight Flask API that powers all product, category, search, and banner data for the app. Built with **Flask** and **MongoDB**, and hosted on **Fly.io**, it seamlessly integrates with the mobile frontend.

**Backend Repo:** [Ajverma2004/clothing-api](https://github.com/Ajverma2004/clothing-api)

### 🔍 Backend Highlights

- RESTful API endpoints for:
  - Fetching all clothing products
  - Getting product details by ID
  - Searching by name, description, or category
  - Retrieving and adding promotional banners
- Category-based filtering and dynamic product discovery
- Deployed using Docker on Fly.io with secure and fast response times

---

## 📐 Architecture Overview

- **MVVM** architecture with proper separation of concerns.
- **Snap Camera Kit SDK** for seamless integration of AR lenses.
- **Gemini AI** backend for chat interactions.
- **Firebase Auth** for secure and scalable user sessions.

---

## 📸 Screenshots

<div align="center">
  <img src="https://github.com/user-attachments/assets/13cebd19-a71d-4d79-b624-4713137a51ca" width="190" height="420"/>
  <img src="https://github.com/user-attachments/assets/b845a3e7-db01-408c-b9b3-d2c4c3540e58" width="190" height="420"/>
  <img src="https://github.com/user-attachments/assets/80a38b54-fe4e-457a-9f3b-38a4f069a3b7" width="190" height="420"/>
  <img src="https://github.com/user-attachments/assets/089f60ad-5696-4a2e-999e-140467d5a0b2" width="190" height="420"/>
  <img src="https://github.com/user-attachments/assets/9dc4a43d-5499-46fe-9454-7f8d42f45757" width="190" height="420"/>
  <img src="https://github.com/user-attachments/assets/4fb5cc62-a976-4df1-b29b-6a2d8b9d0fdb" width="190" height="420"/>
  <img src="https://github.com/user-attachments/assets/c07d93e8-5bd9-449a-bf07-c7d8894e5b23" width="190" height="420"/>
  <img src="https://github.com/user-attachments/assets/dcaf2c54-8fe2-43f3-9a6f-62d538807635" width="190" height="420"/>
  <img src="https://github.com/user-attachments/assets/45b5798b-cd68-4303-9c76-df1bf7ffe86d" width="190" height="420"/>
</div>


---

## 🧠 Future Enhancements

- 📦 Add shopping cart and payment gateway
- 🧬 Integrate AI-based body tracking for size prediction
- 🧾 Generate personalized lookbooks using past try-ons
- 🛒 Wishlist and outfit bookmarking
- 🌐 Multi-language support for international users

---

## 🙌 Inspiration

SnapClothes was created to solve the frustration of guessing how outfits will look when shopping online. By combining **digital fashion design**, **AR technology**, and **AI personalization**, it brings virtual fashion to life in an engaging, interactive way.

---

## 👤 Author

**[Ansh Verma]**  
Final Year BSc Computer Science Student, University of East London  
📍 Based in UK | 🎯 Aspiring Android Engineer  
[LinkedIn](https://linkedin.com/in/AnshV)
