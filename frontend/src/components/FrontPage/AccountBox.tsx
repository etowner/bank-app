import { useState } from "react";
import { useUserContext } from "../../UserContext";
import { Alert, Button, Form, Tab, Tabs, Card, Col, Row } from "react-bootstrap";


const AuthForm = ({ onSubmit, username, password, onUsernameChange, onPasswordChange, error }: 
  { onSubmit: (e: React.MouseEvent<HTMLButtonElement>) => void; username: string; password: string; 
    onUsernameChange: (e: React.ChangeEvent<HTMLInputElement>) => void; 
    onPasswordChange: (e: React.ChangeEvent<HTMLInputElement>) => void; error: string | null }) => (
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
  const { login, register, error, setError } = useUserContext();
  const [username, setUsername] = useState<string>("");
  const [password, setPassword] = useState<string>("");
  const [activeTab, setActiveTab] = useState<string>("create");


  const handleCreate = (event: React.MouseEvent<HTMLButtonElement> | null) => {
    if (!event) return;
    event.preventDefault();
    register(username, password);
  };

  const handleLog = (event: React.MouseEvent<HTMLButtonElement> | null) => {
    if (!event) return;
    event.preventDefault();
    login(username, password);
  };

  const handleTabSwitch = (tab: string | null) => {
    if (tab == null) return;
    setActiveTab(tab);
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
