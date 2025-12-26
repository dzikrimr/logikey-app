<p align="center">
  <img src="https://35chue4gfs.ufs.sh/f/Ox9zBraWD5RJUzLYJ83gsd0y1XLa73Rh2fMKv9tljkASDHe8" width="100%" style="border-radius: 15px;">
</p>

<h1 align="center">
  <b>Logikey - AI-Powered Logical Fallacy Detector Keyboard 🧠⌨️</b>
</h1>

<p align="center">
  <a href="#">
    <img src="https://img.shields.io/badge/Kotlin-1.9.0-blue?logo=kotlin" alt="Kotlin">
  </a>
  <a href="#">
    <img src="https://img.shields.io/badge/Jetpack_Compose-Latest-green?logo=jetpackcompose" alt="Compose">
  </a>
  <a href="#">
    <img src="https://img.shields.io/badge/LLM-Llama--3--8B--Instruct-orange?logo=meta" alt="Llama3">
  </a>
  <a href="#">
    <img src="https://img.shields.io/badge/Adapter-LoRA-red" alt="LoRA">
  </a>
</p>

<p align="center">
  <b>Logikey</b> is an innovative Android keyboard application designed to enhance the quality of digital communication. Unlike standard keyboards, Logikey is powered by a fine-tuned <b>Llama-3 8B</b> model specifically trained to recognize flawed argument structures (logical fallacies) in real-time.
</p>

<p align="center">
  Built with <b>Jetpack Compose</b> and the <b>Logiclyst Pro</b> AI model, this app helps users transform reactive communication habits into more proactive, logical, and persuasive interactions. 💡
</p>

---

## ✨ Core Features (MVP)

Logikey comes with essential features to accompany your every conversation:
- 🕵️ **Real-time LLM Analysis**: Automatically analyzes text using the Llama-3 model when you stop typing for 2-3 seconds.
- 💡 **Logical Argument Suggestions**: Provides in-depth explanations and 3-point logical counter-arguments to strengthen your reasoning.
- 📊 **Insights & Statistics**: Visualizes your communication patterns through interactive "Fallacy Distribution" charts.
- ⚙️ **Dynamic AI Sensitivity**: A slider that dynamically adjusts the AI's *Temperature* ($computed\_temp = \max(0.1, 1.1 - sensitivity)$) for either rigid or creative analysis.
- 🔒 **Local Processing & Privacy**: Analysis history is stored in a local **Room** database. Text data is encrypted when sent to the private API endpoint.

---

## 🛠️ Tech Stack & AI Architecture

### Mobile App (Android)
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose (Modern Declarative UI)
- **Local Database**: Room Persistence
- **State Management**: Kotlin Coroutines & Flow
- **Network**: Retrofit & OkHttp

### AI Backend & Model
- **Base Model**: `meta-llama/Meta-Llama-3-8B-Instruct`
- **Fine-tuning**: PEFT (Parameter-Efficient Fine-Tuning) via **LoRA (Low-Rank Adaptation)**.
- **Quantization**: 4-bit (NF4) / 8-bit for resource efficiency.
- **Framework**: FastAPI (Backend), PyTorch, HuggingFace Transformers.
- **Deployment**: Private Model Adapter Hub via HuggingFace.


---

## 🏗️ Project Structure

This project is divided into two main repositories:
* 📱 **Android Client**: [logikey-app](https://github.com/Dzvelocity/logikey-app) (Current)
* ⚙️ **AI Backend**: [logikey-be](https://github.com/dzikrimr/logikey-be) (FastAPI + Llama-3 Inference)


---

## 🧠 How It Works

Logikey operates at the system level as an **Input Method Service (IME)** with the following workflow:
1. **Input Catching**: The keyboard captures text as the user types.
2. **Dynamic Trigger**: If the user pauses for a specific interval, the text is sent to the **FastAPI Gateway**.
3. **Model Inference**: The backend loads the Llama-3 base model with the **Logiclyst-LoRA** adapter to classify logical fallacies.
4. **Haptic Feedback**: If a fallacy is detected, the device provides haptic feedback, and the keyboard's top bar displays a warning label.



---

## 📸 Prototype & User Interface

<p align="center">
  <img src="https://35chue4gfs.ufs.sh/f/Ox9zBraWD5RJREOULQsDN6HJx37Xub8QqZUSKsIBFY9jzm05" width="100%" style="border-radius: 12px;">
</p>

---

## 🔒 Privacy & Security

Logikey prioritizes user privacy above all:
- **No Keylogging**: We do not store or transmit sensitive data such as passwords or bank account numbers.
- **Private Adapter**: The AI model is hosted on a private HuggingFace repository to prevent unauthorized access to the "golden dataset."
- **Local Storage**: All your text history remains on your device, not on our cloud.

---

## 🧑‍💻 Developer

### [dzvelocity](https://github.com/Dzvelocity): AI & Android Engineer

---

<p align="center">
  🚀 <b>Logikey: Think twice, type once.</b> <br>
  Support this project by giving a ⭐ Star to this repository.
</p>
