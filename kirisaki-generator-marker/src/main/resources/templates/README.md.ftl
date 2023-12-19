# ${name}

> ${description}
>
> 作者: ${author}
>
> 基于 Kirsaki的代码生成器的项目制作,感谢您的使用

可以通过命令行交互式输入的方式动态生成想要的项目代码

## 使用说明
执行项目根目录下的脚本文件
> ./generator.sh 或(./generator.bat) <命令> <参数>

示例命令

```
./generator.sh 或(./generator.bat) generate <#list modelConfig.models as modelInfo> --${modelInfo.fieldName} </#list>
```

<#list modelConfig.models as modelInfo>
${modelInfo?index+1}) ${modelInfo.fieldName}

    类型 ${modelInfo.type}

    描述 ${modelInfo.description}

    默认值 ${modelInfo.defaultValue?c}

<#if modelInfo.abbr??>
    简写 -${modelInfo.abbr}
</#if>


</#list>