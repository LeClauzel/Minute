# Minute Player

**Minute** is a minimalist, non-intrusive audio player for Android designed to feel like a system dialog rather than a full application. It stays out of your way until you need it.

## 🚀 The Concept

Most audio players are heavy, take over your entire screen, and clutter your app drawer. **Minute** is different:
- **No Launcher Icon**: It doesn't appear in your app list. It lives silently on your system.
- **Context-First**: It only wakes up when you share an audio file to it or try to "Open with..." an audio file from your favorite file manager.
- **Overlay UI**: It appears as a floating Material 3 dialog, allowing you to quickly listen to a clip and dismiss it without losing your place in other apps.

## ✨ Features

- **Expressive Controls**: Bouncy, responsive UI buttons for Play/Pause, Replay (5s), and Forward (5s).
- **Speed Control**: Adjust playback speed (1x, 1.5x, 2x, etc.) on the fly.
- **Repeat Modes**: Easily toggle between repeat one and normal playback.
- **Modern Design**: Built entirely with Jetpack Compose and Material 3, featuring smooth animations and a dimmed background overlay.
- **Lightweight**: Powered by Android Media3 (ExoPlayer) for robust and efficient audio decoding.

## 🛠 How to Use

1. **Find an Audio File**: Use any File Manager, Messaging app, or Browser.
2. **Open with Minute**: Tap the file and select **Minute** from the list of apps. Alternatively, use the **Share** menu and pick Minute.
3. **Listen & Dismiss**: The player pops up instantly. Once you're done, tap outside the dialog or finish the audio to go back to what you were doing.

## 🏗 Tech Stack

- **UI**: Jetpack Compose & Material 3
- **Engine**: Media3 ExoPlayer
- **Language**: Kotlin
- **Architecture**: MVVM (ViewModel + StateFlow)

---
*Built for speed. Built for focus. Just a Minute.*
