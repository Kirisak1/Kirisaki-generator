<!DOCTYPE html>
<html>
    <head>
        <title>Kirsiaki</title>
    </head>
    <body>
        <h1>欢迎</h1>
            <ul>
                <#list menuItems as menuItem>
                <li><a href="${menuItem.url}">${menuItem.label}</a></li>
                 </#list>
            </ul>
        <footer>
            ${currentYear} 官网, ALL right reserved
        </footer>
    </body>
</html>