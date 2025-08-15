import React from 'react';
import { Button, Form, Table, Container } from 'react-bootstrap';

const OffersForEvent = () => {
  return (
    <Container className="mt-4">
      <h2 className="mb-4">Manage Offers</h2>
      <Form className="mb-4">
        <Form.Group className="mb-3">
          <Form.Label>Offer Title</Form.Label>
          <Form.Control type="text" placeholder="Enter offer title" />
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>Description</Form.Label>
          <Form.Control as="textarea" rows={3} placeholder="Enter description" />
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>Start Date</Form.Label>
          <Form.Control type="date" />
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>End Date</Form.Label>
          <Form.Control type="date" />
        </Form.Group>

        <Button variant="success" type="submit">Add Offer</Button>
      </Form>

      <Table striped bordered hover>
        <thead>
          <tr>
            <th>Title</th>
            <th>Description</th>
            <th>Start</th>
            <th>End</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>Special Discount</td>
            <td>Get 20% off for early bird</td>
            <td>2025-08-10</td>
            <td>2025-08-20</td>
            <td>
              <Button variant="warning" size="sm" className="me-2">Edit</Button>
              <Button variant="danger" size="sm">Delete</Button>
            </td>
          </tr>
        </tbody>
      </Table>
    </Container>
  );
};

export default OffersForEvent;