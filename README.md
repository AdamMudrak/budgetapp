# Moneta BudgetApp

## 📌 Introduction
Moneta BudgetApp is a modern and secure budget management application designed to help users
efficiently track their incomes, expenses, and savings. With seamless authentication via Telegram
or email, Moneta BudgetApp provides an intuitive and feature-rich experience to help users gain financial control.

## 🚀 Features
- **User Authentication and Password Management:** Sign up and log in using Telegram or email. <br>Reset password via email (for email users) or get a new password through Telegram bot authentication.
    - [AuthController](src/main/java/com/example/budgetingapp/controllers/AuthController.java)
- **Transaction Management:** Add, update, delete, and retrieve income and expense records categorized by date, year, month, accounts, and categories.
    - [IncomeTransactionController](src/main/java/com/example/budgetingapp/controllers/transactions/IncomeTransactionController.java)
    - [ExpenseTransactionController](src/main/java/com/example/budgetingapp/controllers/transactions/ExpenseTransactionController.java)
- **Savings Targets:** Manage savings goals by adding, replenishing, retrieving, and deleting targets.
    - [TargetController](src/main/java/com/example/budgetingapp/controllers/TargetController.java)
- **Account Management:** Create, update, retrieve, and delete financial accounts. Set default accounts.
    - [AccountController](src/main/java/com/example/budgetingapp/controllers/AccountController.java)
- **Budgeting:** Set and manage budgets for specific categories and periods.
    - [BudgetController](src/main/java/com/example/budgetingapp/controllers/BudgetController.java)
- **Support Contact:** Send inquiries to the support team via email (without authentication).
    - [SupportController](src/main/java/com/example/budgetingapp/controllers/SupportController.java)
- **Transfers:** Manage fund transfers between different accounts in the same currency.
    - [TransferController](src/main/java/com/example/budgetingapp/controllers/TransferController.java)
- **Category Management:** Add, update, delete, and retrieve income and expense categories.
    - [IncomeCategoryController](src/main/java/com/example/budgetingapp/controllers/categories/IncomeCategoryController.java)
    - [ExpenseCategoryController](src/main/java/com/example/budgetingapp/controllers/categories/ExpenseCategoryController.java)

## 🛠️ Technologies Used
Moneta BudgetApp is built with a robust and scalable technology stack:

### **Backend Technologies**
- **Spring Security** – Securing the application with JWT authentication.
- **Spring Data JPA** – For seamless database interactions.
- **Liquibase** – For database versioning and migrations.
- **Hibernate Validator** – For data validation.
- **Spring Mail (SendGrid)** – For email notifications and password resets.
- **Telegram Bot API** – For Telegram-based authentication.
- **MapStruct** – For mapping DTOs and entities.
- **JWT (JSON Web Tokens)** – For access, refresh, and action token management.
- **MySQL Database** – MySQL for local testing in volatile containers and for production.

### **Documentation**
- **SpringDoc OpenAPI (Swagger UI)** – For API documentation and testing.

## 🔧 Setup and Installation
Not ready for setting up my application locally yet? Then explore our [Team Project](https://landing.moneta-api.space/) first!<br>There, you will be able to have a look at Swagger documentation, my running telegram bot and my colleague's work - WebSite for the frontend done by [Maria Shmakova](https://www.linkedin.com/in/mariashmakova/)

Follow these steps to set up Moneta BudgetApp locally or follow steps in this [video](https://www.youtube.com/watch?v=OUsDDkUCCTE):

1. **Prerequisites:**
    - If you want to use your **own** MySQL, update [application.properties](src/main/resources/application.properties) directly or [envrironment variables](.env.sample) with your MySQL credentials.
        - If not, just proceed with the next step as follow-up commands are ready to start MySQL locally in docker container.
    - Having a telegram bot with /start and /stop command is **a must** for authentication. Please visit [BotFather](https://t.me/BotFather) and create a bot with aforementioned commands. Use [this](BOTINSTRUCTION.md) instruction to create your bot.
    - Having a sendgrid API key or adjusting [EmailService](src/main/java/com/example/budgetingapp/services/email/EmailService.java) to use Google SMTP **is recommended** but can be ignored on condition you are okay with not being able to use email registration.
        - Having a [SendGrid account](https://sendgrid.com/en-us) **is required** if using Google SMTP is unwanted;
            - Having a spare email address to use for sendgrid **is required**;
    - Software **required**:
        - Git
        - Maven
        - Docker
    ```sh
      #check if everything is installed
      #by checking version of software
      git -v
      mvn -v
      docker -v
    ```

2. **Run the application:**
    ```sh
      #clone the repository
      git clone https://github.com/AdamMudrak/budgetapp.git
      #change to budgetapp root package
      cd budgetapp/
    ```
    ```sh
      #build application archive
      mvn clean package
    ```
    ```sh
      #build application docker image
      docker build -t budgetapp .
    ```
   ```sh
      #pull mysql docker image
      docker pull mysql:latest
    ```
    ```sh
      #run mysql docker container, save data in /var/lib/mysql
      docker run --name mysql-container \
        -e MYSQL_ROOT_PASSWORD=root \
        -e MYSQL_DATABASE=moneta_db \
        -e MYSQL_USER=moneta_user \
        -e MYSQL_PASSWORD=moneta_pass \
        -p 3307:3306 -v mysql_data:/var/lib/mysql -d mysql
    ```
    - Adjusting [application.properties](src/main/resources/application.properties) directly or [envrironment variables](.env.sample) **required**:
        - TELEGRAM_TOKEN - provide Bot API Token to enable telegram authentication
    - **Recommended**, but app will start without them:
        - JWT_SECRET - secure your JWTs
        - EMAIL_SECRET - secure your email links with a random string
        - REGISTRATION_CONFIRMATION_LINK
        - PASSWORD_RESET_CONFIRMATION_LINK
        - GET_RANDOM_PASSWORD_REDIRECT_LINK
        - ORIGINS_ALLOWED
        - SENDGRID_API_KEY - key required to enable [EmailService](src/main/java/com/example/budgetingapp/services/email/EmailService.java)
        - MAIL_ADDRESS - your spare mail address used and verified on [SendGrid](https://sendgrid.com/en-us)
    ```sh
      #run application using .env.sample
      docker run -p 8080:8080 --env-file .env.sample budgetapp
    ```

3. **Access the API documentation:**
    - Navigate to [Swagger UI](http://localhost:8080/swagger-ui/index.html#/) for API exploration.

## ⚠️ Challenges Faced & Solutions
Building Moneta BudgetApp presented some interesting challenges:
- **Security Implementation:** Managing JWT-based authentication for access, refresh, and action tokens required deep integration with Spring Security. Since I had only worked with access tokens before, implementing refresh and action tokens was a new challenge that demanded thorough research and deep learning.
- **Telegram Bot Authentication:** Configuring the Telegram bot to work seamlessly with our authentication system required careful API handling. As I was unfamiliar with the Telegram API, I had to explore its documentation extensively to ensure a secure and efficient integration.
- **Email Handling with SendGrid:** Setting up email-based password recovery and support requests while ensuring security and reliability. Working with the SendGrid API and Spring Email for the first time required an in-depth study of their capabilities to implement a robust and secure email system.

Despite these challenges, overcoming them made the journey incredibly rewarding!

## 📜 License
Moneta BudgetApp is released under [Non-Commercial Use License Agreement](LICENSE.md).

---

🌟 **Enjoy seamless budget tracking with Moneta BudgetApp!**

Still have some questions? Don't hesitate to [reach out](https://www.linkedin.com/in/adam-mudrak-7813b3279/)!
