# Moneta BudgetApp

## 📌 Introduction
Moneta BudgetApp is a modern and secure budget management application designed to help users
efficiently track their incomes, expenses, and savings. With seamless authentication via Telegram
or email, Moneta BudgetApp provides an intuitive and feature-rich experience to help users gain financial control.

## 🚀 Features

## Support API Controller
Send inquiries to the support team via email (without authentication).

- **POST**: `/support/send-request-to-email` - Send email request to our team (no authentication required).
- [SupportController](src/main/java/com/example/budgetingapp/controllers/SupportController.java)

## Authentication API Controller
Sign up and log in using Telegram or email. Reset password via email (for email users) or get
a new password through Telegram bot authentication. Change password.

- **POST**: `/auth/register` - Register a new user in the app.
- **POST**: `/auth/login-email` - Log in using existing email account.
- **POST**: `/auth/login-telegram` - Log in using existing telegram account.
- **POST**: `/auth/change-password` - Change password while being logged in.
- **POST**: `/auth/forgot-password` - Initiate password reset via a link sent to your email.

- [AuthController](src/main/java/com/example/budgetingapp/controllers/AuthController.java)

## Expense & Income Transactions API Controller
Add, update, delete, and retrieve income and expense records categorized by date, year, month, accounts, and categories.

- **POST**: `/income-transactions/add-income` - Add income.
- **POST**: `/expense-transactions/add-expense` - Add expense.

- **GET**: `/income-transactions/get-all-incomes` - Retrieve all incomes, or optionally filter them by: accountId, fromDate, toDate, categoryIds.
- **GET**: `/income-transactions/get-all-incomes-for-charts-days` - Retrieve all incomes grouped by day, then grouped by categories.
- **GET**: `/income-transactions/get-all-incomes-for-charts-months-years` - Retrieve all incomes grouped by month or year, then grouped by categories.

- **GET**: `/expense-transactions/get-all-expenses` - Retrieve all expenses, or optionally filter them by: accountId, fromDate, toDate, categoryIds.
- **GET**: `/expense-transactions/get-all-expenses-for-charts-days` - Retrieve all expenses grouped by day, then grouped by categories.
- **GET**: `/expense-transactions/get-all-expenses-for-charts-months-years` - Retrieve all expenses grouped by month or year, then grouped by categories.

- **PUT**: `/income-transactions/update-income/{transactionId}` - Update income.
- **PUT**: `/expense-transactions/update-expense/{transactionId}` - Update expense.

- **DELETE**: `/income-transactions/delete-income/{transactionId}` - Delete income.
- **DELETE**: `/expense-transactions/delete-expense/{transactionId}` - Delete expense.

- [IncomeTransactionController](src/main/java/com/example/budgetingapp/controllers/transactions/IncomeTransactionController.java)
- [ExpenseTransactionController](src/main/java/com/example/budgetingapp/controllers/transactions/ExpenseTransactionController.java)

## Account API Controller
Create, update, retrieve financial accounts. Set default accounts.

- **POST**: `/account/add-account` - Add a new user's account.
- **GET**: `/account/get-account-by-id/{accountId}` - Get user's account by id.
- **GET**: `/account/get-account-by-default` - Get the account by default.
- **GET**: `/account/get-all-accounts` - Get all user's account.
- **PUT**: `/account/update-account/{accountId}` - Update a user's account name.
- **PUT**: `/account/set-account-by-default/{accountId}` - Set a new account by default.

- [AccountController](src/main/java/com/example/budgetingapp/controllers/AccountController.java)

## Transfers API Controller
Manage fund transfers between different accounts in the same currency.

- **POST**: `/transfers/add-transfer` - Add transfer.
- **GET**: `/transfers/get-all-transfers` - Retrieve all transfers.
- **DELETE**: `/transfers/delete-transfer/{transferId}` - Delete a transfer by its id.

- [TransferController](src/main/java/com/example/budgetingapp/controllers/TransferController.java)

## Expense & Income Category API Controller
Add, update, delete, and retrieve income and expense categories.

- **POST**: `/income-categories/add-category` - Add income category.
- **POST**: `/expense-categories/add-category` - Add expense category.
- **GET**: `/income-categories/get-all-categories` - Get all user's income categories.
- **GET**: `/expense-categories/get-all-categories` - Get all user's expense categories.
- **PUT**: `/income-categories/update-category/{categoryId}` - Update income category.
- **PUT**: `/expense-categories/update-category/{categoryId}` - Update expense category.
- **DELETE**: `/expense-categories/delete-category/{categoryId}` - Delete expense category.
- **DELETE**: `/income-categories/delete-category/{categoryId}` - Delete income category.

- [IncomeCategoryController](src/main/java/com/example/budgetingapp/controllers/categories/IncomeCategoryController.java)
- [ExpenseCategoryController](src/main/java/com/example/budgetingapp/controllers/categories/ExpenseCategoryController.java)

## Target API Controller
Manage savings goals by adding, replenishing, retrieving, and deleting targets.

- **POST**: `/targets/add-target` - Add target.
- **POST**: `/targets/replenish-target` - Replenish target.
- **DELETE**: `/targets/destroy-target` - Destroy a target, retrieve money to one of your accounts.
- **GET**: `/targets/get-all-targets` - Retrieve all targets.

- [TargetController](src/main/java/com/example/budgetingapp/controllers/TargetController.java)

## Budget API Controller
Set and manage budgets for specific categories and periods.

- **POST**: `/budgets/add-budget` - Add a new user's budget.
- **GET**: `/budgets/get-all-budgets` - Get all user's budgets.
- **DELETE**: `/budgets/delete-budget/{budgetId}` - Delete a user's budget.

- [BudgetController](src/main/java/com/example/budgetingapp/controllers/BudgetController.java)

### ⚠️Nota bene!
#### All requests above, except for:
- **POST**: `/support/send-request-to-email`
- **POST**: `/auth/register`
- **POST**: `/auth/login-email`
- **POST**: `/auth/login-telegram`
- **POST**: `/auth/forgot-password`
#### require user to be **AUTHENTICATED** using Bearer Authentication Token, given to you in one these requests:
- **POST**: `/auth/login-email`
- **POST**: `/auth/login-telegram`

## 🛠️ Technologies Used

Moneta BudgetApp leverages a comprehensive and robust technology stack, primarily built on Java, to ensure scalability, security, and maintainability.

### Core Language and Environment

* **Java 17**: The primary programming language for the backend, ensuring modern features and performance.

### Backend Technologies

* **Spring Boot 3.4.4**: The foundation of the application, providing rapid application development and deployment.
* **Spring Security**: Securing the application with JWT authentication, leveraging `io.jsonwebtoken` (version `0.12.6`).
* **Spring Data JPA**: For seamless database interactions, simplifying data access and persistence.
* **Liquibase 4.31.1**: For database versioning and migrations, ensuring consistent database schema management.
* **Hibernate Validator**: For data validation, ensuring data integrity across the application.
* **Spring Mail (SendGrid 4.10.3)**: For email notifications and password resets, integrating with SendGrid's email delivery service.
* **Telegram Bot API 6.9.7.1**: For Telegram-based authentication and interaction, enabling seamless user access via Telegram.
* **MapStruct 1.6.3**: For mapping DTOs and entities, reducing boilerplate code and improving mapping efficiency.
* **JWT (JSON Web Tokens) 0.12.6**: For access, refresh, and action token management, ensuring secure authentication and authorization.
* **MySQL Database 8.0.33**: MySQL for local testing in volatile containers and for production, providing a reliable relational database.
* **SpringDoc OpenAPI Starter WebMVC UI 2.8.6**: For API documentation and testing, enabling easy interaction with the application's endpoints.
* **Lombok 1.18.36**: For reducing boilerplate code, improving code readability and maintainability.

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
