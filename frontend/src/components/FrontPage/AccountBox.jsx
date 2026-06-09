import React, { useState, useContext } from "react";
import { UserContext } from "../../UserContext";
import { Alert, Button, Form, Tab, Tabs, Card, Col, Row } from "react-bootstrap";
import PropTypes from "prop-types";

const AuthForm = ({ onSubmit, username, password, onUsernameChange, onPasswordChange, error }) => (
  AuthForm.propTypes = {
    onSubmit: PropTypes.func.isRequired,
    username: PropTypes.string.isRequired,
    password: PropTypes.string.isRequired,
    onUsernameChange: PropTypes.func.isRequired,
    onPasswordChange: PropTypes.func.isRequired,
    error: PropTypes.string
  },
    <Form>
        <Form.Group as={Row} className="mb-3 justify-content-md-center">
            <Form.Label column sm={6}>Enter username:</Form.Label>
            <Col>
                <Form.Control autoComplete="on" value={username} onChange={onUsernameChange} />
            </Col>
        </Form.Group>
        <Form.Group as={Row} className="mb-3 justify-content-md-center">
            <Form.Label column>Enter password:</Form.Label>
            <Col sm={6}>
                <Form.Control autoComplete="on" type="password" value={password} onChange={onPasswordChange} />
            </Col>
        </Form.Group>
        <Button variant="light" onClick={onSubmit} size="lg" className="mb-3">
            Submit
        </Button>
        {error && <Alert variant="danger">{error}</Alert>}
    </Form>
);

const AccountBox = () => {
  const { login, register, error, setError } = useContext(UserContext);
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [activeTab, setActiveTab] = useState("create");


  const handleCreate = (event) => {
    event.preventDefault();
    register(username, password);
  };

  const handleLog = (event) => {
    event.preventDefault();
    login(username, password);
  };

  const handleTabSwitch = (tab) => {
    setActiveTab(tab);
    // setUsername("");
    // setPassword("");
    setError(null);
  };

  return (
    <Card style={{ width: "35rem" }} bg="secondary" text="light">
      <Card.Body>
        <Tabs activeKey={activeTab} onSelect={handleTabSwitch} className="mb-3" fill>
          <Tab eventKey="create" title="Create Account">
            <AuthForm onSubmit={handleCreate} username={username} 
            password={password} onUsernameChange={(e) => setUsername(e.target.value)}
            onPasswordChange={(e) => setPassword(e.target.value)} error={error} />
          </Tab>
          <Tab eventKey="log" title="Log In">
            <AuthForm onSubmit={handleLog} username={username} 
            password={password} onUsernameChange={(e) => setUsername(e.target.value)}
            onPasswordChange={(e) => setPassword(e.target.value)} error={error} />
          </Tab>
        </Tabs>
      </Card.Body>
    </Card>
  );
};

export default AccountBox;
