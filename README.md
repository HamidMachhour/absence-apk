# Absence APK

Sample Android attendance app for teacher workflows with Laravel + Mosquitto integration.

## Included in this sample

- Teacher login with Laravel token persistence
- Seance roster fetch and local Room caching
- Attendance mark toggle (present/absent) with absent count
- Offline queue for failed submissions using Room + retry flow
- MQTT subscription to `teacher/{teacherId}/seances`
- Local notification channel for MQTT seance alerts
- Simple Compose UI for login, home, and roster attendance flow

## Project structure

- `app/src/main/java/com/absenceapp/teacher/data` – repositories, API DTOs, data store/session logic
- `app/src/main/java/com/absenceapp/teacher/domain` – domain models
- `app/src/main/java/com/absenceapp/teacher/mqtt` – MQTT client wrapper
- `app/src/main/java/com/absenceapp/teacher/network` – Retrofit client
- `app/src/main/java/com/absenceapp/teacher/ui` – Compose screens and ViewModel
- `app/src/main/java/com/absenceapp/teacher/data/local` – Room database and entities

## Architecture summary

This is intentionally small and clean:

- Compose UI + ViewModel state drives the screens.
- Retrofit handles Laravel REST calls.
- Room stores roster and pending submissions locally.
- DataStore stores token and config values.
- MQTT client subscribes to teacher alerts and exposes the latest event to the UI.
- Attendance sync is queued locally when offline and retried when connectivity returns.

## Local setup

1. Install Android Studio or the Android SDK.
2. Open the project in Android Studio.
3. Set a valid Android SDK path if prompted.
4. Update the default config values in the login form before running:
   - Laravel base URL: e.g. `http://192.168.1.2:8000`
   - MQTT host: your PC LAN IP, not `127.0.0.1` on the phone
   - MQTT port: `1883`
   - MQTT username/password: your Mosquitto credentials
5. Run the app on a physical Android device or emulator.

## Laravel expectations

This client is written to fit the payloads you described:

- Login: `POST /api/auth/login`
- Fetch roster: `GET /api/v1/seances/{seance}/roster`
- Submit attendance: `POST /api/v1/seances/{seance}/attendance`

If the backend shape differs from the examples, the parsing is intentionally tolerant and the DTOs can be adapted with minimal changes.

## MQTT details

The app subscribes to the teacher alert topic:

- `teacher/{teacherId}/seances`

When the payload contains `event = "seance_starting"`, the app keeps the latest message in ViewModel state and exposes it in the UI.

## Offline-first behavior

- Rosters are cached locally in Room.
- When attendance submission fails because the device is offline, the app stores a pending item locally.
- The retry flow can be triggered via the ViewModel when the app is back online.

## Build verification

This workspace was verified with:

```bash
cd /mnt/data_nvme/absence_app_project/absence-apk
export ANDROID_HOME=/usr/lib/android-sdk
export ANDROID_SDK_ROOT=/usr/lib/android-sdk
/home/hamid/.local/gradle-8.7/bin/gradle assembleDebug
```

Result: `BUILD SUCCESSFUL`.

## Notes

- This is a small sample app, not a full production attendance system.
- The app uses a simple local notification channel for MQTT seance alerts.
- If your Laravel response shape differs, review the DTOs in the `data/remote` package and adapt the parser accordingly.
- For a real device, use your computer's LAN IP instead of `127.0.0.1` in both Laravel and MQTT settings.

