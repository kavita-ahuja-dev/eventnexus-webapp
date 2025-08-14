
import React, { useState, useEffect } from 'react';
import { Container, Form, Button } from 'react-bootstrap';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const CreateEvent = () => {
  const navigate = useNavigate();

  const [eventTypes, setEventTypes] = useState([]);
  const [message, setMessage] = useState('');

  const [formData, setFormData] = useState({
    title: '',
    description: '',
    date: '',
    price: '',
    year: '',
    mode: '',
    zoomUrl: '',
    address: '',
    latitude: '',
    longitude: '',
    mapUrl: '',
    typeId: '',
    imageUrl: '',
    exhibitorId: Number(localStorage.getItem('userId')) || null,
  });

  useEffect(() => {
    axios.get('http://localhost:8080/api/event-types')
      .then(res => setEventTypes(res.data))
      .catch(err => console.error('Error fetching event types:', err));
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: typeof value === 'string' ? value.trimStart() : value,
      ...(name === 'date' && value ? { year: new Date(value).getFullYear() } : {})
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMessage('');

  const form = e.currentTarget;
  if (!form.checkValidity()) {
    form.reportValidity();     
    return;
  }

  if (showZoomUrl && !formData.zoomUrl?.trim()) {
    setMessage('Please enter Zoom URL for online events.');
    return;
  }
  if (showAddressFields && !formData.address?.trim()) {
    setMessage('Please enter address for offline events.');
    return;
  }


    const payload = {
      ...formData,
      price: Number(formData.price),
      year: Number(formData.year),
      typeId: Number(formData.typeId),
      exhibitorId: Number(formData.exhibitorId),
      zoomUrl: formData.zoomUrl?.trim() || null,
      address: formData.mode === 'OFFLINE' ? formData.address?.trim() || null : null,
      mapUrl: formData.mode === 'OFFLINE' ? formData.mapUrl?.trim() || null : null,
      latitude: formData.latitude ? Number(formData.latitude) : null,
      longitude: formData.longitude ? Number(formData.longitude) : null,
      imageUrl: formData.imageUrl?.trim() || null,
    };

    console.log('Submitting Event Payload:', payload);

    try {
      //correct working commented
      await axios.post('http://localhost:8080/api/events', payload);
      // await axios.post('http://localhost:8080/api/events', payload, {
      //     headers: {
      //       Authorization: `Bearer ${localStorage.getItem('token')}`
      //     }
      //   });

      setMessage('Event created successfully!');
      //setTimeout(() => navigate('/exhibitor/MyEvents'), 1500);
      setTimeout(() => navigate('/exhibitor/my-events'), 1500);

    } catch (err) {
      console.error('Error:', err);
      setMessage('Error creating event.');
    }
  };

  const showZoomUrl = formData.mode === 'ONLINE';
  const showAddressFields = formData.mode === 'OFFLINE';

  return (
    <Container className="mt-4" style={{ maxWidth: '700px' }}>
      <h2 className="mb-4">Create New Event</h2>

      {message && <div className={`alert ${message.includes('success') ? 'alert-success' : 'alert-danger'}`}>{message}</div>}

      <Form onSubmit={handleSubmit}>
        <Form.Group className="mb-3">
          <Form.Label>Title</Form.Label>
          <Form.Control name="title" value={formData.title} onChange={handleChange} required />
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>Description</Form.Label>
          <Form.Control as="textarea" rows={3} name="description" value={formData.description} onChange={handleChange} />
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>Date</Form.Label>
          <Form.Control type="date" name="date" value={formData.date} onChange={handleChange} required />
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>Price</Form.Label>
          <Form.Control type="number" name="price" value={formData.price} onChange={handleChange} required />
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>Event Mode</Form.Label>
          <Form.Select name="mode" value={formData.mode} onChange={handleChange} required>
            <option value="">Select Mode</option>
            <option value="ONLINE">Online</option>
            <option value="OFFLINE">Offline</option>
          </Form.Select>
        </Form.Group>

        {showZoomUrl && (
          <Form.Group className="mb-3">
            <Form.Label>Zoom URL</Form.Label>
            <Form.Control type="text" name="zoomUrl" value={formData.zoomUrl} onChange={handleChange} />
          </Form.Group>
        )}

        {showAddressFields && (
          <>
            <Form.Group className="mb-3">
              <Form.Label>Address</Form.Label>
              <Form.Control name="address" value={formData.address} onChange={handleChange} />
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label>Latitude</Form.Label>
              <Form.Control name="latitude" value={formData.latitude} onChange={handleChange} />
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label>Longitude</Form.Label>
              <Form.Control name="longitude" value={formData.longitude} onChange={handleChange} />
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label>Map URL</Form.Label>
              <Form.Control name="mapUrl" value={formData.mapUrl} onChange={handleChange} />
            </Form.Group>
          </>
        )}

        <Form.Group className="mb-3">
          <Form.Label>Image URL</Form.Label>
          <Form.Control name="imageUrl" value={formData.imageUrl} onChange={handleChange} placeholder="https://example.com/image.jpg" />
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>Event Type</Form.Label>
          <Form.Select name="typeId" value={formData.typeId} onChange={handleChange} required>
            <option value="">Select Event Type</option>
            {eventTypes.map((type) => (
              <option key={type.id} value={type.id}>{type.name}</option>
            ))}
          </Form.Select>
        </Form.Group>

        <Button type="submit" variant="primary">Create Event</Button>
      </Form>
    </Container>
  );
};

export default CreateEvent;

