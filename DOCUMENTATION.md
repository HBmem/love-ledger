# Love Ledger Documentation

Software Name: Love Ledger

Version: 0.001

Date: Dec 18,2024

# Table of Contents
- [User Stories]()
- [Models]()
    - [User Credentials](#user-credentials)
    - [User Profile](#user-profile)
    - [Love Interest](#love-interest)
    - [Relationship](#relationship)
    - [Milestone](#milestone)
    - [User Milestone](#user-milestone)
    - [Relationship Milestone](#relationship-milestone)
    - [Outing](#outing)
    - [Notable Day](#notable-day)
    - [Sentiment](#sentiment)
    - [Reminder](#reminder)
    - [Idea](#idea)
    - [Idea Feedback](#idea-feedback)
    - [Tag](#tag)
    - [Tag Association](#tag-association)
- [Entity Relationship Diagram](#entity-relationship-diagram)

## User Stories

## Models
### User Credentials
#### Variables
| Variable Name | Relation | DataType | Requirements | Description |
| ------------- | -------- |--------- | ------------ | ----------- |
| user_id | pk | int  | not null |
| username | | varchar(50) | not null, unique |
| email | | varchar(250) | not null, unique |
| password | | | not null |
| is_verified | | boolean | Default: False |
| is_disabled | | boolean | Default: False |
| created_at | | timestamp | not null |
| updated_at | | timestamp | not null |

#### Requirements
 - UserId must not be null
 - Username is unique
 - Username must not be null
 - Email is unique
 - Email is not null
 - Password is not null
 - Password length must be greater than 6 characters

### User Profile
#### Variables
| Variable Name | Relation | DataType | Requirements | Description |
| ------------- | -------- |--------- | ------------ | ----------- |
| user_id | pk, fk(user_credentials) | int  | not null |
| fname | | varchar(100) | not null |
| lname | | varchar(100) | |
| gender | | varchar(50) | not null |
| birthday | | date | not null |
| phone_number| | |
| likes | | JSON | |
| dislikes | | JSON | |
| photo_url | | |
| created_at | | timestamp | not null |
| updated_at | | timestamp | not null |

#### Requirements
- fname must not be null
- gender must not be null
- birthday must not be null

### Love Interest
#### Variables
| Variable Name | Relation | DataType | Requirements | Description |
| ------------- | -------- |--------- | ------------ | ----------- |
| love_interest_id | pk | int | not null |
| nickname | | varchar(50) | not null(if fname is null) |
| fname | | varchar(100) | not null(if nickname is null) |
| lname | | varchar(100) | |
| gender | | varchar(50) | not null |
| birthday | | date | not null |
| likes | | JSON | |
| dislikes | | JSON | |
| photo_url | | | |
| user_id | fk(user_credentials) | int | not null |

### Relationship
#### Variables
| Variable Name | Relation | DataType | Requirements | Description |
| ------------- | -------- |--------- | ------------ | ----------- |
| relationship_id | pk | int | not null |
| start_date | | date | not null |
| end_date | | date |
| relationship_status | | varchar(50) | not null |
| importance_level | | int | not null, check(importance_level >= 1 AND importance_level <= 5) |
| is_official | | boolean | not null, default(0) |
| user_id | fk | int | not null |
| love_interest_id | fk | int | not null |

### Milestone
#### Variables
| Variable Name | Relation | DataType | Requirements | Description |
| ------------- | -------- |--------- | ------------ | ----------- |
| milestone_id | pk | int | not null |
| name | | varchar(255) | not null |
| description | | text | not null |
| type | | varchar(50) | not null |

#### Requirements
- name must not be null
- description must not be null
- type must not be null

### User Milestone
#### Variables
| Variable Name | Relation | DataType | Requirements | Description |
| ------------- | -------- |--------- | ------------ | ----------- |
| user_id | pk, fk | int | not null |
| milestone_id | pk, fk | int | not null |
| date_received | | date | not null |

### Relationship Milestone
#### Variables
| Variable Name | Relation | DataType | Requirements | Description |
| ------------- | -------- |--------- | ------------ | ----------- |
| relationship_id| pk, fk | int | not null |
| milestone_id | pk, fk | int | not null |
| date_received | | date | not null |

### Outing
#### Variables
| Variable Name | Relation | DataType | Requirements | Description |
| ------------- | -------- |--------- | ------------ | ----------- |
| outing_id | pk | int | not null |
| name | | varchar(255) | not null |
| type | | varchar(100) | not null |
| notes | | text |
| photo_url |
| location | | text | not null(if is_valid_address = false)
| street number | | varchar(20) | not null(if is_valid_address = true)
| street_name | | varchar(255) | not null(if is_valid_address = true)
| city | | varchar(100) | not null(if is_valid_address = true)
| state | | varchar(100) | not null(if is_valid_address = true)
| zip_code | | varchar(20) | not null(if is_valid_address = true)
| cost | | decimal(10,2) |
| rating | | int | check(importance_level >= 1 AND importance_level <= 10)
| start_time | | datetime | not null
| end_time | | datetime | not null
| relationship_id | fk | int | not null

### Notable Day
#### Variables
| Variable Name | Relation | DataType | Requirements | Description |
| ------------- | -------- |--------- | ------------ | ----------- |
| notable_day_id | pk | int | not null
| name | | varchar(100) | not null |
| description | | text | not null |
| recurrence_type | | varchar(50) | not null |
| recurrence_rule | | varchar(50) |
| specific_date | | date |
| is_system_defined | | boolean | not null | This is intend to differentiate user created versus predefined (Optional).

### Sentiment
#### Variables
| Variable Name | Relation | DataType | Requirements | Description |
| ------------- | -------- |--------- | ------------ | ----------- |
| sentiment_id | pk | int | not null |
| date | | date | not null |
| mood_score | | int | check(importance_level >= 1 AND importance_level <= 5)
| notes | | text |
| user_id | fk | int | not null
| relationship_id | fk | int | not null

### Reminder
#### Variables
| Variable Name | Relation | DataType | Requirements | Description |
| ------------- | -------- |--------- | ------------ | ----------- |
| reminder_id | pk | int
| name | | varchar(100) | not null
| description | | text |
| reminder_date | | date | not null
| notify_before_days | | int | not null
| is_recurring | | boolean | default(false)
| user_id | fk | int | not null
| relationship_id | fk | int | not null
| love_interest_id | fk | int | not null
| notable_day_id | fk | int | not null

### Idea
#### Variables
| Variable Name | Relation | DataType | Requirements | Description |
| ------------- | -------- |--------- | ------------ | ----------- |
| idea_id | pk | int | not null
| name | | varchar(255) | not null
| description | | text | not null
| type | | varchar(20) | not null
| source | | varchar(255) | not null | Source of the idea, e.g., Predefined, AI, or external API
| is_predefined | | boolean | default(true)
| user_id | fk | int | not null

### Idea Feedback
#### Variables
| Variable Name | Relation | DataType | Requirements | Description |
| ------------- | -------- |--------- | ------------ | ----------- |
| feedback_id | pk | int | not null
| is_liked | | boolean | default(false)
| is_saved | | boolean | default(false)
| rating | | decimal(3, 2) |
| user_id | fk | int | not null
| idea_id | fk | int | not null

### Tag
#### Variables
| Variable Name | Relation | DataType | Requirements | Description |
| ------------- | -------- |--------- | ------------ | ----------- |
| tag_id | pk | int | not null
| name | | varchar(100) | not null
| category | | varchar(50) | not null |

### Tag Association
#### Variables
| Variable Name | Relation | DataType | Requirements | Description |
| ------------- | -------- |--------- | ------------ | ----------- |
| tag_id | fk | int| not null
| entity_id | | int | not null
| entity_type | | varchar(50) | not null

## Entity Relationship Diagram