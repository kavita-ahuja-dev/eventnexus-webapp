
import React, { useEffect, useState, useMemo } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import { Form, Button, Spinner, Alert, Row, Col } from 'react-bootstrap';

const API_BASE = 'http://localhost:8080';


export default function EditEvent() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [event, setEvent] = useState(null);
  const [types, setTypes] = useState([]);              
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState('');

  const eventModes = useMemo(() => ['ONLINE', 'OFFLINE'], []);

  useEffect(() => {
    (async () => {
      try {
        const [evRes, typeRes] = await Promise.all([
          axios.get(`${API_BASE}/api/events/${id}`),
          axios.get(`${API_BASE}/api/event-types`) // adjust if your endpoint is different
        ]);

        const ev = evRes.data || {};
        // ensure date is yyyy-MM-dd for <input type="date">
        const dateStr = (ev.date || '').slice ? ev.date.slice(0, 10) : ev.date;

        setEvent({
          id: ev.id,
          title: ev.title || '',
          description: ev.description || '',
          date: dateStr || '',
          mode: ev.mode || 'OFFLINE',         // ONLINE/OFFLINE
          zoomUrl: ev.zoomUrl || '',
          address: ev.address || '',
          latitude: ev.latitude ?? '',
          longitude: ev.longitude ?? '',
          mapUrl: ev.mapUrl || '',
          price: ev.price ?? '',
          year: ev.year ?? '',
          typeId: ev.typeId || ev.type?.id || '', // whichever your API returns
          isActive: ev.isActive ?? true
        });

        setTypes(typeRes.data || []);
      } catch (err) {
        console.error(err);
        setError(err?.response?.data?.message || 'Failed to load event details.');
      } finally {
        setLoading(false);
      }
    })();
  }, [id]);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setEvent(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value
    }));
  };

  const handleNumber = (e) => {
    const { name, value } = e.target;
    setEvent(prev => ({
      ...prev,
      [name]: value === '' ? '' : Number(value)
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSaving(true);
    setError('');

    const payload = {
      id: event.id,                
      title: event.title.trim(),
      description: event.description.trim(),
      date: event.date,             
      mode: event.mode,            
      zoomUrl: event.mode === 'ONLINE' ? event.zoomUrl.trim() : null,
      address: event.mode === 'OFFLINE' ? event.address.trim() : null,
      latitude: event.mode === 'OFFLINE' && event.latitude !== '' ? Number(event.latitude) : null,
      longitude: event.mode === 'OFFLINE' && event.longitude !== '' ? Number(event.longitude) : null,
      mapUrl: event.mode === 'OFFLINE' ? event.mapUrl.trim() : null,
      price: event.price === '' ? 0 : Number(event.price),
      year: event.year === '' ? null : Number(event.year),
      typeId: event.typeId ? Number(event.typeId) : null,
      isActive: !!event.isActive
    };

    try {
      await axios.put(`${API_BASE}/api/events/${id}`, payload);
      navigate('/exhibitor/my-events');
    } catch (err) {
      console.error(err);
      setError(err?.response?.data?.message || 'Failed to update event.');
    } finally {
      setSaving(false);
    }
  };

  if (loading) return <div className="container mt-4"><Spinner animation="border" /></div>;
  if (error)   return <div className="container mt-4"><Alert variant="danger">{error}</Alert></div>;
  if (!event)  return null;

  return (
    <div className="container mt-4">
      <h2>Edit Event</h2>
      <Form onSubmit={handleSubmit}>
        <Row>
          <Col md={6}>
            <Form.Group className="mb-3">
              <Form.Label>Title</Form.Label>
              <Form.Control
                name="title"
                value={event.title}
                onChange={handleChange}
                required
              />
            </Form.Group>
          </Col>

          <Col md={3}>
            <Form.Group className="mb-3">
              <Form.Label>Date</Form.Label>
              <Form.Control
                type="date"
                name="date"
                value={event.date || ''}
                onChange={handleChange}
                required
              />
            </Form.Group>
          </Col>

          <Col md={3}>
            <Form.Group className="mb-3">
              <Form.Label>Year</Form.Label>
              <Form.Control
                type="number"
                name="year"
                value={event.year}
                onChange={handleNumber}
                min="1900"
                max="2100"
              />
            </Form.Group>
          </Col>
        </Row>

        <Form.Group className="mb-3">
          <Form.Label>Description</Form.Label>
          <Form.Control
            as="textarea"
            rows={3}
            name="description"
            value={event.description}
            onChange={handleChange}
          />
        </Form.Group>

        <Row>
          <Col md={4}>
            <Form.Group className="mb-3">
              <Form.Label>Event Type</Form.Label>
              <Form.Select
                name="typeId"
                value={event.typeId || ''}
                onChange={handleChange}
                required
              >
                <option value="">-- Select type --</option>
                {types.map(t => (
                  <option key={t.id} value={t.id}>{t.name || t.typeName || `Type ${t.id}`}</option>
                ))}
              </Form.Select>
            </Form.Group>
          </Col>

          <Col md={4}>
            <Form.Group className="mb-3">
              <Form.Label>Mode</Form.Label>
              <Form.Select
                name="mode"
                value={event.mode}
                onChange={handleChange}
                required
              >
                {eventModes.map(m => (
                  <option key={m} value={m}>{m}</option>
                ))}
              </Form.Select>
            </Form.Group>
          </Col>

          <Col md={4}>
            <Form.Group className="mb-3">
              <Form.Label>Price</Form.Label>
              <Form.Control
                type="number"
                name="price"
                value={event.price}
                onChange={handleNumber}
                step="0.01"
                min="0"
                required
              />
            </Form.Group>
          </Col>
        </Row>

        {event.mode === 'ONLINE' ? (
          <Form.Group className="mb-3">
            <Form.Label>Zoom URL</Form.Label>
            <Form.Control
              name="zoomUrl"
              value={event.zoomUrl}
              onChange={handleChange}
              placeholder="https://..."
            />
          </Form.Group>
        ) : (
          <>
            <Form.Group className="mb-3">
              <Form.Label>Address</Form.Label>
              <Form.Control
                name="address"
                value={event.address}
                onChange={handleChange}
                placeholder="Venue address"
              />
            </Form.Group>

            <Row>
              <Col md={4}>
                <Form.Group className="mb-3">
                  <Form.Label>Latitude</Form.Label>
                  <Form.Control
                    type="number"
                    name="latitude"
                    value={event.latitude}
                    onChange={handleNumber}
                    step="0.000001"
                  />
                </Form.Group>
              </Col>
              <Col md={4}>
                <Form.Group className="mb-3">
                  <Form.Label>Longitude</Form.Label>
                  <Form.Control
                    type="number"
                    name="longitude"
                    value={event.longitude}
                    onChange={handleNumber}
                    step="0.000001"
                  />
                </Form.Group>
              </Col>
              <Col md={4}>
                <Form.Group className="mb-3">
                  <Form.Label>Map URL</Form.Label>
                  <Form.Control
                    name="mapUrl"
                    value={event.mapUrl}
                    onChange={handleChange}
                    placeholder="https://maps.google.com/?q=..."
                  />
                </Form.Group>
              </Col>
            </Row>
          </>
        )}

        <Form.Group className="mb-4">
          <Form.Check
            type="switch"
            id="isActiveSwitch"
            label="Active"
            name="isActive"
            checked={!!event.isActive}
            onChange={handleChange}
          />
        </Form.Group>

        <div className="d-flex gap-2">
          <Button type="submit" variant="primary" disabled={saving}>
            {saving ? 'Saving…' : 'Save Changes'}
          </Button>
          <Button type="button" variant="secondary" onClick={() => navigate('/exhibitor/my-events')}>
            Cancel
          </Button>
        </div>
      </Form>
    </div>
  );
}
