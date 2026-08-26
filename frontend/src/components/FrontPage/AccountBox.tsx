import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Alert, Button, Form, Tab, Tabs, Card, Col, Row } from "react-bootstrap";
import { registerUser, loginUser } from "../../api/userApi";
import { getAxiosError } from "../../api/axiosConfig";
interface AuthFormProps {
  idPrefix: string; 
  onSubmit: (e: React.MouseEvent<HTMLButtonElement>) => void;
  username: string;
  password: string;
  onUsernameChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  onPasswordChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  error: string | null;
}

const AuthForm = ({ idPrefix, onSubmit, username, password, 
                    onUsernameChange, onPasswordChange, error }: AuthFormProps) => (
  <Form>
    <Form.Group as={Row} controlId={`${idPrefix}-username`} className="mb-3 justify-content-md-center">
      <Form.Label column sm={6}>Enter username:</Form.Label>
      <Col>
        <Form.Control autoComplete="username" value={username} onChange={onUsernameChange} />
      </Col>
    </Form.Group>
    <Form.Group as={Row} controlId={`${idPrefix}-password`} className="mb-3 justify-content-md-center">
      <Form.Label column>Enter password:</Form.Label>
      <Col sm={6}>
        <Form.Control autoComplete="current-password" type="password" value={password} onChange={onPasswordChange} />
      </Col>
    </Form.Group>
    <Button variant="light" onClick={onSubmit} size="lg" className="mb-3">
      Submit
    </Button>
    {error && <Alert variant="danger">{error}</Alert>}
  </Form>
);

const AccountBox = () => {
  const [error, setError] = useState<string | null>(null);
  const [username, setUsername] = useState<string>("");
  const [password, setPassword] = useState<string>("");
  const [activeTab, setActiveTab] = useState<string>("create");
  const navigate = useNavigate();
  
  const register = async (username: string, password: string) => {
    setError(null);
    try {
      await registerUser(username, password);
    } catch (err) {
      setError(getAxiosError(err));
      console.error("Registration error:", err);
      return; 
    }
    void navigate(`/home`);
  };

  const login = async (username: string, password: string) => {
    setError(null);
    try {
      await loginUser(username, password );
    } catch (err) {
      setError(getAxiosError(err));
      console.error("Login error:", err);
      return; 
    }
    void navigate(`/home`);
  };

  const handleCreate = (event: React.MouseEvent<HTMLButtonElement> | null) => {
    if (!event) return;
    event.preventDefault();
    void register(username, password);
  };

  const handleLog = (event: React.MouseEvent<HTMLButtonElement> | null) => {
    if (!event) return;
    event.preventDefault();
    void login(username, password);
  };

  const handleTabSwitch = (tab: string | null) => {
    if (tab == null) return;
    setActiveTab(tab);
    setError(null);
  };

  return (
    <Card style={{ width: "35rem" }} bg="secondary" text="light">
      <Card.Body>
        <Tabs activeKey={activeTab} onSelect={handleTabSwitch} className="mb-3" unmountOnExit fill>
          <Tab eventKey="create" title="Create Account">
            <AuthForm idPrefix="create" onSubmit={handleCreate} username={username} 
            password={password} onUsernameChange={(e) => setUsername(e.target.value)}
            onPasswordChange={(e) => setPassword(e.target.value)} error={error} />
          </Tab>
          <Tab eventKey="log" title="Log In">
            <AuthForm idPrefix="log" onSubmit={handleLog} username={username} 
            password={password} onUsernameChange={(e) => setUsername(e.target.value)}
            onPasswordChange={(e) => setPassword(e.target.value)} error={error} />
          </Tab>
        </Tabs>
      </Card.Body>
    </Card>
  );
};

export default AccountBox;
