import { useEffect, useState } from "react";
import { Card, Col, Container, Row, Alert } from "react-bootstrap";
import { useUserContext } from "../../context/UserContext";
import { createAccount } from "../../api/accountApi";
import Transfer from "./Transfer";
import Header from "./Header";
import OpenAccount from "./OpenAccount";
import PieChart from "./PieChart";
import AccountList from "./AccountList";
import "../../styles/Home.css";

const Home = () => {
  const { user, username, fetchUser } = useUserContext();
  const accounts = user?.accounts ?? [];
  const numOfAccounts = user?.numOfAccounts ?? 0;
  const [error, setError] = useState<string | null>(null);
  
  const openAcc = async (type: string) => {
    if (numOfAccounts >= 3) {
      setError("You can only have 3 accounts");
      return;
    }
    try {
      const newAccount = await createAccount(type);
      console.log("Opened account:", newAccount);
      await fetchUser();
    } catch (err) {
      setError("Failed to open account. Please try again later.");
      console.error(err);
    }

  };

  useEffect(() => {
    void fetchUser();
  }, [fetchUser]);

  return (
    <div className="Home">
      <Header />
      <Container>
        <Row className="mb-5">
          <h2>Welcome {username}</h2>
        </Row>
        <Row xs={1} md={2} lg={2} className="g-4">
          {/* Lists Accounts and provides open account options */}
          <Col key={1}>
            <Card border="dark">
              <AccountList accounts={accounts} />
              <OpenAccount
                openAcc={(type) => void openAcc(type)}
                setError={setError}
              />
              {error && (
                <Alert className="" variant="danger">
                  {error}
                </Alert>
              )}
            </Card>
          </Col>
          <Col key={2}>
            <Transfer />
          </Col>
          <Col key={3}>
            <Card style={{ width: "32rem" }} className="mb-2">
              <PieChart accounts={accounts} />
            </Card>
          </Col>
        </Row>
      </Container>
    </div>
  );
};

export default Home;
