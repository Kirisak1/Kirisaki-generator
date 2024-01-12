import AuthorInfo from '@/pages/Generator/Detail/components/AuthorInfo';
import FileConfig from '@/pages/Generator/Detail/components/FileConfig';

import { getGeneratorVoByIdUsingGet } from '@/services/backend/generatorController';
import { useParams } from '@@/exports';
import { DownloadOutlined } from '@ant-design/icons';
import { PageContainer } from '@ant-design/pro-components';
import { Button, Card, Col, message, Row, Space, Tabs, Tag, Typography } from 'antd';
import moment from 'moment';
import React, { useEffect, useState } from 'react';
import ModelConfig from "@/pages/Generator/Detail/components/ModelConfig";

const GeneratorAddPage: React.FC = () => {
  const { id } = useParams();
  const [data, setData] = useState<API.GeneratorVO>({});
  const [loading, setLoading] = useState<boolean>(true);

  /**
   * 加载数据
   */
  const loadData = async () => {
    if (!id) {
      return;
    }
    setLoading(true);
    try {
      const res = await getGeneratorVoByIdUsingGet({ id });
      if (res.data) {
        setData(res.data || {});
      }
    } catch (error: any) {
      message.error('加载数据失败' + error.message);
    }
    setLoading(false);
  };
  useEffect(() => {
    if (id) {
      loadData();
    }
  }, [id]);

  const tagListView = (tags?: string[]) => {
    if (!tags) {
      return <></>;
    }
    return (
      <div style={{ marginBottom: 8 }}>
        {tags.map((tag) => (
          <Tag key={tag}> {tag}</Tag>
        ))}
      </div>
    );
  };
  return (
    <PageContainer loading={loading}>
      <Card>
        <Row justify={'space-between'} gutter={[32, 32]}>
          <Col flex="auto">
            <Space size="large" align="center">
              <Typography.Title level={4}>{data.name}</Typography.Title>
              {tagListView(data.tags)}
            </Space>
            <Typography.Paragraph>{data.description}</Typography.Paragraph>
            <Typography.Paragraph type="secondary">
              创建时间:{moment(data.createTime).format('YYYY-MM-DD HH:mm:ss')}
            </Typography.Paragraph>
            <Typography.Paragraph type="secondary">基础包:{data.basePackage}</Typography.Paragraph>
            <Typography.Paragraph type="secondary">版本:{data.version}</Typography.Paragraph>
            <Typography.Paragraph type="secondary">作者:{data.author}</Typography.Paragraph>
            <div style={{ marginBottom: 24 }} />
            <Space size="middle">
              <Button type="primary">立即使用</Button>
              <Button icon={<DownloadOutlined />}>下载</Button>
            </Space>
          </Col>
          <Col flex="320px">
            <img src={data.picture} />
          </Col>
        </Row>
      </Card>
      <div style={{ marginBottom: 24 }} />
      <Card>
        <Tabs
          size={'large'}
          defaultActiveKey={'fileConfig'}
          onChange={() => {}}
          items={[
            {
              key: 'fileConfig',
              label: '文件配置',
              children: <FileConfig data={data} />,
            },
            {
              key: 'modelConfig',
              label: '模型配置',
              children: <ModelConfig data={data} />,
            },
            {
              key: 'AuthorInfo',
              label: '作者信息',
              children: <AuthorInfo data={data} />,
            },
          ]}
        />
      </Card>
    </PageContainer>
  );
};
export default GeneratorAddPage;
