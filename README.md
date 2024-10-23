# MapsActivity - Android App

Este proyecto es una aplicación Android que muestra la ubicación en tiempo real utilizando la **API de Google Maps** y los servicios de **Fused Location Provider**. La aplicación puede mostrar la posición actual del usuario y actualizarla en tiempo real a medida que el usuario se mueve.

## Características

- Visualización de la ubicación actual del usuario en un mapa.
- Actualización de la ubicación en tiempo real con un marcador que se mueve conforme el usuario cambia de posición.
- Integración con la **API de Google Maps**.
- Solicitud de permisos de ubicación en tiempo de ejecución.
  
## Requisitos

Para ejecutar este proyecto necesitarás:

- Android Studio (versión recomendada: 2020.3.1 o superior)
- Un dispositivo físico o un emulador con servicios de Google Play.
- API Key de Google Maps.

-------------------------------------------------------------------------------------------
# Vulnerabilities Report

## Summary
- Total vulnerabilities found: 4
- Critical: 0
- High:3
- Medium: 1
- Low:

## Detailed Findings

### 1. 

- ** Descripcion:** App can be installed on a vulnerable upatched Android version
Android 7.0, [minSdk=24]
- ** Severity:** High
- ** Impact: ** 
- ** Steps to Reproduce: **
- ** Remediation:** Suppression the rule vulnerable_os_version in com.kmo.mpasconmultimedia

### 2. Debug Enabled For App
[android:debuggable=true]

- ** Descripcion:** Debugging was enabled on the app which makes it easier for reverse engineers to hook a debugger
to it. This allows dumping a stack trace and accessing debuggig helper classes.
- ** Severity:** High
- ** Impact: ** 
- ** Steps to Reproduce: **
- ** Remediation:** nose

### 3.Application Data can be Backed up:

- ** Descripcion:** This flag allows anyone to backup your application data via adb. It allows users who have enabled USB debugging to copy application data off of the device.
- ** Severity:** warning
- ** Impact: ** 
- ** Steps to Reproduce: **
- ** Remediation:** nose

### 3.Broadcast Receiver

- ** Descripcion:** A Broadcast Receiver is found to be shared with other apps on the device therefore leaving it accessible to any other application on the device. It is protected by a permission which is not defined in the analysed application. As a result, the protection level of the permission should be checked where it is defined. If it is set to normal or dangerous, a malicious application can request and obtain the permission and interact with the component. If it is set to signature, only applications signed with the same certificate can obtain the permission.
- ** Severity:** warning
- ** Impact: ** 
- ** Steps to Reproduce: **
- ** Remediation:** nose

### 4.Permission

- ** Descripcion:** Allows an application to read from external storage
- ** Severity:** dangerous
- ** Impact: ** 
- ** Steps to Reproduce: **
- ** Remediation:** nose
