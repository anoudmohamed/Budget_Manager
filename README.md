# Personal Budget Manager

## Overview
Personal Budget Manager is a comprehensive Java application designed to help users manage their personal finances effectively. The application offers a complete suite of budgeting tools, expense tracking, income management, and financial goal setting features in a secure environment.

## Features

### User Authentication
- Secure signup and login system with email verification
- Password validation ensuring strong security standards
- OTP (One-Time Password) verification during registration
- Phone number validation

### Expense Management
- Track expenses with detailed information (amount, category, payment method, date)
- View all recorded expenses
- Generate summary and detailed expense reports

### Income Tracking
- Record income from multiple sources
- Validate income entries with proper formatting
- Track income history

### Budgeting
- Set budget limits for different spending categories
- View all budget allocations
- Comprehensive spending analysis comparing actual expenses to budget limits

### Financial Goals
- Set savings goals with target amounts and deadlines
- Track progress toward financial goals
- View all active goals

### Reminders
- Create payment and financial reminders
- Schedule notifications with date and time
- Format validation for all reminder fields

### Reporting
- Generate different types of financial reports (summary and detailed)
- View spending patterns and trends
- Analyze budget performance

## Technical Implementation

### Design Patterns
- **Strategy Pattern**: For generating different types of reports
- **Singleton Pattern**: Ensures single instance of critical components
- **Validator Pattern**: For input validation

### Security Features
- Password encryption
- OTP verification for new registrations
- Input validation on all fields

### Data Storage
- File-based storage system for user data, expenses, incomes, goals, and reminders
- Proper data segregation to maintain privacy between users

## Classes and Interfaces Overview

### Core Classes
- **PersonalBudget**: Main class containing the application entry point and user interface logic
- **User**: Class representing a user account with personal information and financial data
- **Expense**: Class for storing and managing expense transactions
- **Income**: Class for tracking and validating income entries
- **Budget**: Class for setting spending limits by category
- **Goal**: Class for setting and tracking financial goals
- **Reminder**: Class for creating and validating financial reminders

### Authentication Classes
- **AuthenticationManager**: Class handling user registration and login
- **UserValidator**: Class for validating user input (email, password, phone)
- **OTPGenerator**: Utility class for generating one-time passwords
- **OTPService**: Class for sending OTP verification to users

### Storage Classes
- **UserStorage**: Interface defining methods for user data persistence
- **FileUserStorage**: Implementation class for file-based user storage

### Reporting Classes
- **Report**: Class for generating financial reports using strategy pattern
- **ReportStrategy**: Interface defining report generation behavior
- **SummaryReport**: Implementation class for generating summary financial reports
- **DetailedReport**: Implementation class for generating detailed expense reports

### Analysis Classes
- **SpendingAnalysis**: Class for analyzing spending patterns against budget limits

## Installation and Usage

### Prerequisites
- Java Runtime Environment (JRE) 8 or higher

### Running the Application
1. Compile the application:
   ```
   javac PersonalBudget.java
   ```

2. Run the application:
   ```
   java PersonalBudget
   ```

3. Follow the on-screen instructions to sign up or log in

### Initial Setup
1. Create an account using a valid email, password, and phone number
2. Verify your account with the OTP sent to your email
3. Log in to access the dashboard

### Dashboard Options
1. Add Expense
2. Display Expenses
3. Add Reminder
4. Add Goal
5. Display Goals
6. View Financial Report
7. Track My Income
8. Budgeting & Analyzing
9. Logout

## Best Practices
- Regularly track all expenses and income
- Set realistic budget targets
- Review your spending analysis monthly
- Update your financial goals as needed

