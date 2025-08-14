import React, { useEffect, useState } from 'react';
import { Card, Button, Container, Row, Col, Spinner, Alert } from 'react-bootstrap';
import { Link } from "react-router-dom";

import axios from 'axios';

const ExploreEvents = () => {
  const [events, setEvents] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    axios
      .get('http://localhost:8080/api/events') 
      .then((res) => {
        setEvents(res.data);
        setLoading(false);
      })
      .catch((err) => {
        setError('Failed to load events. Try again later.');
        setLoading(false);
      });
  }, []);

  return (
    <Container className="mt-4">
      <h2 className="mb-4">Explore Upcoming Events</h2>

      {loading && <Spinner animation="border" />}
      {error && <Alert variant="danger">{error}</Alert>}

      <Row>
        {events.map((event) => (
          <Col md={4} key={event.id}>
            <Card className="mb-4">
              <Card.Body>
                <Card.Title>{event.title}</Card.Title>
                <Card.Text>
                  Date: {event.date} <br />
                  Mode: {event.mode || 'TBA'} <br />
                  Location: {event.Address || 'TBA'} <br />
                  Type: {event.eventTypeName || 'General'}
                </Card.Text>
                {/* <Button variant="primary" disabled>
                  View Details
                </Button> */}
                <Button
                        as={Link}
                          to={`/events/${event.id}`}
                          variant="primary"
                      >
                        View Details
                      </Button>
              </Card.Body>
            </Card>
          </Col>
        ))}

        {events.length === 0 && !loading && !error && (
          <Col>
            <Alert variant="info">No events found.</Alert>
          </Col>
        )}
      </Row>
      <div align="right">
          <Link to="/customer/dashboard" className=" bg-green-100 rounded hover:bg-green-200" align="right">
                Go to Customer Dashboard
              </Link>
      </div>
    </Container>
  );
};

export default ExploreEvents;
