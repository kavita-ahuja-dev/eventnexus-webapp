import React from 'react';
import { Form, Button, Container } from 'react-bootstrap';

const UploadEventImage = () => {
  return (
    <Container className="mt-4">
      <h2 className="mb-4">Upload Event Image</h2>
      <Form>
        <Form.Group className="mb-3">
          <Form.Label>Select Event</Form.Label>
          <Form.Select>
            <option>Select event</option>
            <option value="1">Tech Summit</option>
            <option value="2">Art Expo</option>
          </Form.Select>
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>Choose Image</Form.Label>
          <Form.Control type="file" accept="image/*" />
        </Form.Group>

        <Button variant="primary" type="submit">Upload</Button>
      </Form>
    </Container>
  );
};

export default UploadEventImage;
