import React from 'react';
import { Card } from 'react-bootstrap';

const StatCard = ({ title, count, icon }) => {
  return (
    <Card className="text-center shadow-sm">
      <Card.Body>
        {icon && <div className="mb-2">{icon}</div>}
        <Card.Title>{count}</Card.Title>
        <Card.Text>{title}</Card.Text>
      </Card.Body>
    </Card>
  );
};

export default StatCard;
