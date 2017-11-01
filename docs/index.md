# Blossom

Blossom is a Java framework which enables you to realize your projects quickly and smoothly.
It comes with several modules and tools made to ease and speed up your development process.

## Quick start

Sadly, there is no quick start yet :'-(

You must clone the repository, build, install it manually, then you can use the project generator to start your first project with Blossom.

1. git clone https://github.com/mgargadennec/blossom.git
2. cd blossom
3. mvn clean install
4. java -jar blossom-tools/blossom-tools-initializr/target/blossom-tools-initializr-0.0.1-SNAPSHOT.jar
5. Go to localhost:8090
6. Choose your modules and generate your project !

## How-to

### Menu
#### Add a new menu entry
To add a new menu entry, juste add a MenuItem bean.
You can use the capabilities of an injected MenuItemBuilder to simplifies it's creation.

key() is mandatory and represents the id of the menu.
label() is the i18n key or not static text
icon() is the css class that will be applied on the icon
link() is the relative url of the page the menu should lead to.
privilege() set the right the logged user should have to see the menu
order() allow to sort the menu entries. (values ranging from Integer.MIN_VALUE to Integer.MAX_VALUE)
leaf() indicates that the menu is a leaf in the hierarchy. If a non-leaf menu has no submenu, it'll not be displayed.

    @Bean
    public MenuItem testMenuItem(MenuItemBuilder builder) {
        return builder.key("testMenu").label("menu.test", true).icon("fa fa-photo").link("/blossom/test").build();
    }

#### Add a new menu subentry
To add a submenu entry, use the same process than to add a menu, but set the parent property by injecting the parent MenuItem using the @Qualifier annotation.

    @Bean
    public MenuItem testSubMenuItem(MenuItemBuilder builder, @Qualifier("testMenuItem") MenuItem testMenuItem) {
        return builder.key("testSubMenu").label("menu.test.submenu", true).link("/blossom/test/submenu").icon("fa fa-photo").parent(testMenuItem).build();
    }

#### Open a specific menu on a page
#### Ordering menus

### Scheduling
#### Configuring the scheduler
#### Add a scheduled task

### Adding an entity
#### Manually create an entity
#### Index and search an entity
#### Generate a (simple) entity

### Adding an association between two entities
#### Manually create an association

## Features

### Minimal features

### Back-office
Blossom comes with a back-office interface (module : blossom-ui-web) which can be accessed on http://localhost:8080/blossom.
You can then connect with the default user and password : system / system.

This default user can be configured with the following properties :

    blossom.security.default.account.enabled=true
    blossom.security.default.account.identifier=system
    blossom.security.default.account.password=system

### Security

### Caching

### Application Events

### Plugin system

### Indexation and Search

### Bean Validation

### Exception Handling

### Action tokens

### Rendering

### Liquibase

### Scheduling

### I18n

### Mails


