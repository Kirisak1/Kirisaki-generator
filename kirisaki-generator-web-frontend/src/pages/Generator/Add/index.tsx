import FileUploader from '@/components/FileUploader';
import PictureUploader from '@/components/PictureUploader';
import { addGeneratorUsingPost } from '@/services/backend/generatorController';
import {
  ProCard,
  ProFormInstance,
  ProFormItem,
  ProFormSelect,
  ProFormText,
  ProFormTextArea,
  StepsForm,
} from '@ant-design/pro-components';
import { history } from '@umijs/max';
import { message } from 'antd';
import { useRef } from 'react';

const GeneratorAddPage: React.FC = () => {
  const formRef = useRef<ProFormInstance>();
  const doSubmit = async (values: API.GeneratorAddRequest) => {
    if (!values.fileConfig) {
      values.fileConfig = {};
    }
    if (!values.modelConfig) {
      values.modelConfig = {};
    }

    if (values.distPath && values.distPath.length > 0) {
      //@ts-ignore
      values.distPath = values.distPath[0].response;
    }
    try {
      const res = await addGeneratorUsingPost(values);
      message.success('创建成功');
      history.push(`/generator/detail/${res.data}`);
    } catch (error: any) {
      message.error('创建失败' + error.message);
    }
  };
  return (
    <ProCard>
      <StepsForm<API.GeneratorAddRequest> formRef={formRef} onFinish={doSubmit}>
        <StepsForm.StepForm
          name="base"
          title="基本信息"
          onFinish={async () => {
            console.log(formRef.current?.getFieldsValue());
            return true;
          }}
        >
          <ProFormText name="name" label="名称" placeholder="请输入名称" />
          <ProFormTextArea name="description" label="描述" placeholder="请输入描述" />
          <ProFormText name="basePackage" label="基础包" placeholder="请输入基础包" />
          <ProFormText name="version" label="版本" placeholder="请输入版本" />
          <ProFormText name="author" label="作者" placeholder="请输入作者" />
          <ProFormSelect name="tags" label="标签" placeholder="请输入标签列表" mode="tags" />
          <ProFormItem label='图片' name='picture'>
            <PictureUploader biz={'generator_picture'} />
          </ProFormItem>
        </StepsForm.StepForm>
        <StepsForm.StepForm name="fileConfig" title="文件配置"></StepsForm.StepForm>
        <StepsForm.StepForm name="modelConfig" title="模型配置"></StepsForm.StepForm>
        <StepsForm.StepForm name="dist" title="生成器文件">
          <ProFormItem label='产物包' name='distPath'>
            <FileUploader biz={'generator_dist'} description={'请上传生成器文件压缩包'} />
          </ProFormItem>
        </StepsForm.StepForm>
      </StepsForm>
    </ProCard>
  );
};
export default GeneratorAddPage;
