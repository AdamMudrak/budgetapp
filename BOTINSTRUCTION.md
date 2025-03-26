# Setting Up a Telegram Bot with BotFather

Follow these steps to create a Telegram bot using **BotFather** and obtain its API token.

## Step 1: Start a Chat with BotFather
1. Open Telegram and search for **BotFather** or open it [here](https://t.me/BotFather).
2. Start a conversation by clicking **Start**.

## Step 2: Create a New Bot
Copy and paste the following message into the chat with BotFather:

```
/newbot
```

- BotFather will ask for a **name** and a **username** for your bot.
- The **bot name** can be anything (e.g., MonetaBot).
- The **username** must be unique and end with `bot` (e.g., MonetaFinanceBot).

After completing this step, **BotFather will provide an API token**. **Save this token securely!**

## Step 3: Set Commands for Your Bot
To define commands (`/start` and `/stop`), send the following message:

```
/setcommands
```

- Select your bot from the list.
- Enter the following commands:
  ```
  start - Start the bot
  stop - Stop the bot
  ```

Now your bot has the `/start` and `/stop` commands registered.

## Step 4: Use the API Token
Once you receive the bot token, you can use it in your **Spring Boot** application to connect with Telegram’s API.

---

Your Telegram bot is now set up and ready to use! 🚀