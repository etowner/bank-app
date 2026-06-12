import { useEffect, useState } from "react";
import { Card, Col, Container, Row, Alert } from "react-bootstrap";
import Transfer from "./Transfer";
import AccList from "./AccList";
import Header from "./Header";
import "../../styles/Home.css";
import OpenAccount from "./OpenAccount";
import PieChart from "./PieChart";
import { useUserContext } from "../../UserContext";
import { createAccount } from "../../api/accountApi";
// import { User } from "../../types";

const Home = () => {
  const { user, username, fetchUser } = useUserContext();
  const accList = user?.accounts ?? [];
  const [error, setError] = useState<string | null>(null);

  const openAcc = async (type: string) => {
    if( accList.length >= 3){
      setError("You can only have 3 accounts");
      return;
    }
    try {
       
      const newAccount = await createAccount(type);
      console.log("Opened account:", newAccount);
    } catch (error) {
      console.error(error);
    }

    fetchUser(); 
  };

  useEffect(() => {
    fetchUser();
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
              <AccList accList={accList} />
              <OpenAccount openAcc={openAcc} setError={setError} />
              {error && <Alert className="" variant="danger">{error}</Alert>}
            </Card>
          </Col>
          <Col key={2}>
            <Transfer />
          </Col>
          <Col key={3}>
            <Card style={{ width: "32rem" }} className="mb-2">
              <PieChart accounts={accList} />
            </Card>
          </Col>
        </Row>
      </Container>
    </div>
  );
};

export default Home;
