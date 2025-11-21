# hackaton
Source code for project "My first Hedera Account" for the hackaton

Main objectives of the project: Make Web3 hyper simple. Just start the desktop app and you get a full Hedera Account ready to get started in a personal wallet or for coding. It even comes already with 10 tiny bars of balance

Detailed description will be in the project's hackaton page

This repo contains 3 projects:
- The Desktop app, should run anywhere java runs: Windows, Linux, Mac
- The backend server app, should run anywhere java runs. For this demo we are using Google Cloud Run
- The frontend web, this is what we used to demo the account creation from a web browser

## The backend server
It is a private process that creates a new Hedera account and returns its data to the calller Desktop App, both the Desktop App and the Fronted App internally call this service

## The Desktop App
It is a simple window that shows you your new Hedera account ready to be used in testnet

## The frontned
Simple html web to allow the user to create a Hedera account from any web browser